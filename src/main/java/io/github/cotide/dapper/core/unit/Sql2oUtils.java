package io.github.cotide.dapper.core.unit;


import io.github.cotide.dapper.basic.domain.Entity;
import io.github.cotide.dapper.basic.enums.EnumMapping;
import io.github.cotide.dapper.basic.enums.IEnum;
import io.github.cotide.dapper.basic.enums.IntegerEnum;
import io.github.cotide.dapper.basic.enums.StringEnum;
import io.github.cotide.dapper.exceptions.SqlBuildException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Sql2oUtils {


    public static String getLambdaColumnName(Serializable lambda) {
        for (Class<?> cl = lambda.getClass(); cl != null; cl = cl.getSuperclass()) {
            try {
                Method m = cl.getDeclaredMethod("writeReplace");
                m.setAccessible(true);
                Object replacement = m.invoke(lambda);
                if (!(replacement instanceof SerializedLambda)) {
                    break; // custom interface implementation
                }
                SerializedLambda serializedLambda = (SerializedLambda) replacement;
                return Sql2oCache.getLambdaColumnName(serializedLambda);
            } catch (NoSuchMethodException e) {
                // do nothing
            } catch (IllegalAccessException | InvocationTargetException e) {
                break;
            }
        }
        return null;
    }


    public static  <TEntity extends Entity>  Object getValue(TEntity model,Field field)
    {
        try {
            field.setAccessible(true);
            Object value = field.get(model);
            if (null != value) {
                // 枚举处理
                if (value instanceof Enum) {
                    if(value instanceof StringEnum)
                    {
                       return value.toString();
                    }else if(value instanceof IntegerEnum)
                    {
                        return ((IEnum)value).getCode();
                    }
                    EnumMapping enumMapping = field.getAnnotation(EnumMapping.class);
                    if (null == enumMapping) {
                        return value.toString();
                    } else {
                        if (enumMapping.value().equals(EnumMapping.TO_STRING)) {
                           return value.toString();
                        }
                        if (enumMapping.value().equals(EnumMapping.ORDINAL)) {
                           return ((Enum) value).ordinal();
                        }
                    }
                }
                return value;
            }else{
               return null;
            }

        } catch (IllegalAccessException e) {
            throw new SqlBuildException("illegal argument or Access:"+e.getMessage());
        }
    }

   public static  <TEntity extends Entity>  List<Object> getValues(TEntity model)  {

        List<Object> columnValueResult =  new ArrayList<>();

        for (Field field : Sql2oCache.computeModelFields(model.getClass()))
        {
            columnValueResult.add(getValue(model,field));
        }
        return columnValueResult;
    }

}
