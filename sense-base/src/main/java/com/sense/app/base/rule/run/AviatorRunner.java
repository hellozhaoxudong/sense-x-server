package com.sense.app.base.rule.run;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.exception.*;

import java.util.Map;

/**
 * Aviator脚本执行
 * 只会抛出BusAviatorException一种异常
 * @ClassName AviatorRunner
 * @Author zhaoxudong
 * @Date 2022/12/16
 **/
public class AviatorRunner {

    public static String runScriptReString(String scriptKey, String script, Map<String, Object> params) {
        Object result = runScriptReObj(scriptKey, script, params);
        return String.valueOf(result);
    }

    private static Object runScriptReObj(String scriptKey, String script, Map<String, Object> params) {
        try {
            // 1. 编译脚本
            Expression runner = AviatorEvaluator.getInstance().compile(scriptKey, script, true);

            // 2.执行脚本获取返回值
            Object outData = runner.execute(params);
            return outData;
        } catch (UnsupportedFeatureException e){
            return "[运行错误] 脚本中使用了不支持的特性" + e.getMessage();
        } catch (ExpressionSyntaxErrorException e){
            return "[运行错误] 脚本语法错误" + e.getMessage();
        } catch (CompareNotSupportedException e){
            return "[运行错误] 比较不支持异常" + e.getMessage();
        } catch (CompileExpressionErrorException e){
            return "[运行错误] 脚本编译异常" + e.getMessage();
        } catch (ExpressionNotFoundException e){
            return "[运行错误] 脚本编译时未找到异常" + e.getMessage();
        } catch (FunctionNotFoundException e){
            return "[运行错误] 脚本中函数未找到错误" + e.getMessage();
        } catch (LoadScriptFailureException e){
            return "[运行错误] 脚本加载发生异常" + e.getMessage();
        } catch (NoSuchPropertyException e){
            return "[运行错误] 在Java对象中未找到属性错误" + e.getMessage();
        } catch (IllegalArityException e){
            return "[运行错误] 非法函数数量异常" + e.getMessage();
        } catch (ExpressionRuntimeException e){
            return "[运行错误] 脚本运行时异常" + e.getMessage();
        } catch (Exception e){
            if(e instanceof StandardError){
                return "[运行错误] 脚本运行时异常" + e.getMessage();
            }else {
                return "[运行错误] 脚本运行异常" + ExceptionUtil.stacktraceToOneLineString(e);
            }
        }
    }
}
