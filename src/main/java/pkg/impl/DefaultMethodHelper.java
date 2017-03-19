package pkg.impl;

import com.google.common.base.Throwables;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by ubuntu on 3/19/17.
 */
public class DefaultMethodHelper {
    static final Constructor<MethodHandles.Lookup> constructor;

    static {
        try {
            constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        } catch (NoSuchMethodException e) {
            throw Throwables.propagate(e);
        }
        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
    }

    static Cache<Class<?>, MethodHandles.Lookup> lookupCache = CacheBuilder.newBuilder().build();//c -> constructor.newInstance(c, MethodHandles.Lookup.PRIVATE));
    static Cache<Method, MethodHandle> methodCache = CacheBuilder.newBuilder().build();//c -> constructor.newInstance(c, MethodHandles.Lookup.PRIVATE));

    public static Object invokeWithDefault(Method method, Object proxy, Object impl, Object[] args) {
        if (method.isDefault()) {
            final Class<?> declaringClass = method.getDeclaringClass();
            try {
                return
                        methodCache.get(method, () ->
                                lookupCache.get(declaringClass, () -> constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE))
                                        .unreflectSpecial(method, declaringClass))
                                .bindTo(proxy)
                                .invokeWithArguments(args);
            } catch (Throwable throwable) {
                throw Throwables.propagate(throwable);
            }
        } else {
            try {
                return method.invoke(impl, args);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }


}
