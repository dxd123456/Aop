package com.zking.demo.Log.Aspect;


import com.zking.demo.Log.face.ControllerLog;
import com.zking.demo.Log.face.ServiceLog;
import com.zking.demo.mapper.UserMapper;
import com.zking.demo.model.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

@Aspect
@Component
public class Aspects {
    @Resource
    private UserMapper userMapper;
    //本地异常日志记录对象
    private static final Logger logger = LoggerFactory.getLogger(Aspects.class);
    //Controller层切点
    @Pointcut("@annotation(com.zking.demo.Log.face.ControllerLog)")
    public  void controllerAspect() {

    }
    //Service层切点
    @Pointcut("@annotation(com.zking.demo.Log.face.ServiceLog)")
    public void serviceAspect() {

    }
        /**
         * 前置通知 用于拦截Controller层记录用户的操作
         * @param joinPoint 切点
         */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String ip =request.getRemoteAddr();
        //*========控制台输出=========*//
        try {
            System.out.println("========前置通知========");
            System.out.println("请求方法"+(joinPoint.getTarget().getClass().getName()));
            System.out.println("方法描述"+getControllerMethodDescription(joinPoint));
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("请求ip"+ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param joinPoint
     * @return
     * @throws Exception
     */
    @AfterThrowing(pointcut = "serviceAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable e){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session =  request.getSession();
        //读取session中的用户
        User user = (User) session.getAttribute("user");
        //获取请求ip
        String ip = request.getRemoteAddr();
        //获取用户请求方法的参数并序列化为JSON格式字符串
        String params = "";
        try {
            System.out.println("=====异常通知开始=====");
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            System.out.println("方法描述:" + getServiceMthodDescription(joinPoint));
            //System.out.println("请求人:" + user.getUname());
            System.out.println("请求IP:" + ip);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private String getControllerMethodDescription(JoinPoint joinPoint)throws Exception {
        String  targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments  = joinPoint.getArgs();
        Class targeClass = Class.forName(targetName);
        Method[] methods = targeClass.getMethods();
        String description = "";
        for (Method method : methods) {
            if(method.getName().equals(methodName)){
                Class[] clazzs = method.getParameterTypes();
                if(clazzs.length==arguments.length){
                    description = method.getAnnotation(ControllerLog .class).description();
                    break;
                }
            }
        }
        return  description;
    }
    private String getServiceMthodDescription(JoinPoint joinPoint) throws Exception {
           String  targetName = joinPoint.getTarget().getClass().getName();
           String methodName = joinPoint.getSignature().getName();
           Object[] arguments  = joinPoint.getArgs();
           Class targeClass = Class.forName(targetName);
           Method[] methods = targeClass.getMethods();
           String description = "";
        for (Method method : methods) {
             if(method.getName().equals(methodName)){
                 Class[] clazzs = method.getParameterTypes();
                 if(clazzs.length==arguments.length){
                     description = method.getAnnotation(ServiceLog.class).description();
                     break;
                 }

             }
        }
          return description;
    }
}
