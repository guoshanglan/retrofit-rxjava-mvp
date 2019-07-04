package cc.hisens.hardboiled.patient.utils;



import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.lang.reflect.Array;

import cc.hisens.hardboiled.patient.ui.activity.main.MainActivity;




//js封装的类,java代码直接跑js
public class JsEngine {
public Context context;



    /**
     * 执行JS
     *
     * @param js js代码
     * @param functionName js方法名称
     * @param functionParams js方法参数
     * @return
     */

    public Object runScript(String js, String functionName, Object[] functionParams) {
        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);
        try {
            Scriptable scope = rhino.initStandardObjects();

            ScriptableObject.putProperty(scope, "javaContext", Context.javaToJS(this, scope));
            ScriptableObject.putProperty(scope, "javaLoader", Context.javaToJS(MainActivity.class.getClassLoader(), scope));

            rhino.evaluateString(scope, js, "MainActivity", 1, null);

            Function function = (Function) scope.get(functionName, scope);

            Object result = function.call(rhino, scope, scope, functionParams);
            if (result instanceof String) {
                return (String) result;
            } else if (result instanceof NativeJavaObject) {
                return (String) ((NativeJavaObject) result).getDefaultValue(String.class);
            } else if (result instanceof NativeObject) {
                return (String) ((NativeObject) result).getDefaultValue(String.class);
            } else if (result instanceof NativeArray) {

                return  ((NativeArray) result).getDefaultValue(Array.class);
            }
            return result.toString();//(String) function.call(rhino, scope, scope, functionParams);
        } finally {
            Context.exit();
        }
    }





}
