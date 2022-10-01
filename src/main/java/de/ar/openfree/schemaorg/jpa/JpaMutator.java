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
        adaptee = adaptee_;
        wrapper = new BeanWrapperImpl(adaptee);
        changes = new HashMap<>();
    }

    public static <T extends Mutable<?>> Object newInstance(Schema adaptee, Class<T> cls) {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{cls, ChangeSet.class},
                new JpaMutator(adaptee));
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
        switch (method.getName()) {
            case "setChangeListener":
                this.setChangeListener((ChangeListener) args[0]);
                return null;
            case "getChanges":
                return this.getChanges();
            case "save":
                return this.save();
            default:
                return this.mutate(proxy, method, args);
        }
    }

    private Schema save() {
        changes.forEach((propertyName, change) -> this.wrapper.setPropertyValue(propertyName, change.getCurrentValue()));
        changeListener.onChange(this);
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
