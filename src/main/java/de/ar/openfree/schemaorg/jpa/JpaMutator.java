package de.ar.openfree.schemaorg.jpa;

import de.ar.openfree.schemaorg.*;
import lombok.Setter;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.stream.Collectors;

public class JpaMutator implements InvocationHandler, ChangeSet {

    private final Schema adaptee;
    private final Map<String, Change> changes;
    private final BeanWrapper wrapper;

    @Setter
    private ChangeListener changeListener;

    JpaMutator(Schema adaptee_) {
        this(adaptee_, null);
    }

    JpaMutator(Schema adaptee_, ChangeListener listener_) {
        adaptee = adaptee_;
        wrapper = new BeanWrapperImpl(adaptee);
        changes = new HashMap<>();
        changeListener = listener_;
    }

    public static <T extends Mutable<?>> T newInstance(Schema adaptee, Class<T> cls) {
        return newInstance(adaptee, cls, null);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Mutable<?>> T newInstance(
            Schema adaptee,
            Class<T> cls,
            ChangeListener listener) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{cls},
                new JpaMutator(adaptee, listener));
    }

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
    public List<Change> getChanges() {
        return new ArrayList<>(changes.values());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        if ("save".equals(method.getName())) {
            return this.save();
        }
        return this.mutate(proxy, method, args);
    }

    private Schema save() {
        changes.forEach((propertyName, change) -> this.wrapper.setPropertyValue(propertyName, change.getCurrentValue()));
        if(changeListener != null) {
            changeListener.onChange(this);
            changeListener = null;
        }
        return adaptee;
    }

    private Object mutate(Object proxy, Method method, Object[] args) {
        checkMutator(method, args);
        var propertyName = method.getName();
        var previousValue = wrapper.getPropertyValue(propertyName);
        var currentValue = args[0];
        if (!Objects.equals(previousValue, currentValue)) {
            changes.put(propertyName, new Change(propertyName, previousValue, currentValue));
        } else {
            changes.remove(propertyName);
        }
        return proxy;
    }

    private void checkMutator(Method method, Object[] args) {
        var isMutator = args.length == 1
                && method.getName().matches("^((?!get))|^(?!set)[a-z].*$");
        if (!isMutator) {
            throw new SchemaException("Cannot proxy method " + method.getName());
        }
    }

    public String toString() {
        return "ChangeSet(adaptee="
                + this.adaptee.getLabel()
                + ", changes="
                + this.getChanges().stream()
                .map(Change::getProperty)
                .collect(Collectors.toList())
                + ")";
    }
}
