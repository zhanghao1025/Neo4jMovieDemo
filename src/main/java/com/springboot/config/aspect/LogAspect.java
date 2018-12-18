package com.springboot.config.aspect;

import com.google.gson.Gson;
import com.springboot.pojo.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hai on 2018年3月10日.
 */
@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String requestPath = null ; // 请求地址
    private String userName = null ; // 用户名
    private Map<?,?> inputParamMap = null ; // 传入参数
    private Map<String, Object> outputParamMap = null; // 存放输出结果
    private long startTimeMillis = 0; // 开始时间
    private long endTimeMillis = 0; // 结束时间

    /**
     *
     * @Title：doBeforeInServiceLayer
     * @Description: 方法调用前触发
     *  记录开始时间
     * @author hai
     * @date 2018年3月10日
     * @param joinPoint
     */
    @Before(value = "(@annotation(org.springframework.web.bind.annotation.GetMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PutMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
        startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
        this.startPrintOptLog();
    }

    /**
     *
     * @Title：doAfterInServiceLayer
     * @Description: 方法调用后触发
     *  记录结束时间
     * @author hai
     * @date 2018年3月10日
     * @param joinPoint
     */
    @After(value = "(@annotation(org.springframework.web.bind.annotation.GetMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PutMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
        endTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间
        this.endPrintOptLog();
    }

    /**
     *
     * @Title：doAround
     * @Description: 环绕触发
     * @author hai
     * @date 2018年3月10日
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "(@annotation(org.springframework.web.bind.annotation.GetMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PutMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {

        /*MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        String methodName = method.getName(); //获取被拦截的方法名
        */
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();//获取request信息
        // 获取输入参数
        inputParamMap = request.getParameterMap();
        userName = request.getParameter("username");
        // 获取请求地址
        requestPath = request.getRequestURI();

        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
        outputParamMap = new HashMap<>();
        Object result = null;
        try{
            if (userName!=null){
                result = pjp.proceed();// result的值就是被拦截方法的返回值
            }else {
                result = Result.fail("用戶未登錄");
            }
        }catch (Exception e){
            logger.error(e.getMessage());
            result = Result.fail("用戶未登錄");
        }
        outputParamMap.put("result", result);
        return result;
    }

    /**
     *
     * @Title：printOptLog
     * @Description: 输出日志
     * @author tengfei.zhang
     * @date 2017年10月13日 下午4:47:09
     */
    private void startPrintOptLog() {
        Gson gson = new Gson(); // 需要用到google的gson解析包
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);
        logger.info("\n user："+ userName +"  url："+ requestPath +"; param："+ gson.toJson(inputParamMap)+ ";startTime："+ startTime);
    }
    /**
     *
     * @Title：printOptLog
     * @Description: 输出日志
     * @author tengfei.zhang
     * @date 2017年10月13日 下午4:47:09
     */
    private void endPrintOptLog() {
        Gson gson = new Gson(); // 需要用到google的gson解析包
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTimeMillis);
        logger.info("\n user："+ userName +"  result："+gson.toJson(outputParamMap)+ "endTime："+endTime);
    }
}
