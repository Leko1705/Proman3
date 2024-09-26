package proman.plugin;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class BeanClassExtensionPoint extends ExtensionPoint {


    public BeanClassExtensionPoint(String name, Class<?> epClass) {
        super(name, epClass);
    }


    @Override
    protected void registerExtension(Map<String, String> args) {
        Class<?> beanClass = getEpClass();
        Object bean = instance(beanClass.getName());

        if (bean == null)
            return;

        Set<String> missing = new HashSet<>(args.keySet());

        for (Field field : beanClass.getFields()) {

            if (!field.isAnnotationPresent(Attribute.class)) continue;
            String havingAttributeName = field.getAnnotation(Attribute.class).value();

            if (!args.containsKey(havingAttributeName)) {
                if (!field.isAnnotationPresent(Required.class))
                    continue;

                // TODO log missing attribute for field field.getName()
                return;
            }

            missing.remove(havingAttributeName);

            if (field.getType() == String.class) {
                setField(field, bean, args.get(havingAttributeName));
                continue;
            }

            Class<?> argType;
            try {
                argType = Class.forName(args.get(havingAttributeName));
            }
            catch (ClassNotFoundException e) {
                // TODO log error
                return;
            }

            if (!field.getType().isAssignableFrom(argType)) {
                // TODO log type mismatch
                //  -> can not assign args.get(havingAttributeName) to field.getType()
                return;
            }

            Object arg;
            try {
                arg = argType.getConstructor().newInstance();
            }
            catch (Exception e) {
                // TODO log error (unexpected instantiation exception; maybe non-empty constructor?)
                return;
            }

            setField(field, bean, arg);
        }

        if (!missing.isEmpty()){
            throw new IllegalArgumentException("Unexpected attributes: " + missing + " for class " + beanClass.getName());
        }

        registerExtension(bean);
    }

    private void setField(Field field, Object instance, Object value) {
        field.setAccessible(true);
        try {
            field.set(instance, value);
        }
        catch (Exception e) {
            // TODO log error
        }
    }
}
