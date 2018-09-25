package dao.impl;

import dao.CodeSerialRepository;
import dao.MenuRepository;
import models.CodeConfig;
import models.CodeSerial;
import models.Menu;
import models.QuestionFeedback;
import play.Logger;
import play.db.jpa.JPAApi;
import views.html.helper.select;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPACodeSerialRepository implements CodeSerialRepository {
    private final Logger.ALogger logger = Logger.of(JPAQuestionFeedbackRepository.class);

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPACodeSerialRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }
    @Override
    public String getCodeInfo(int configID){
        String codeConfig = "";//配置流水号
        CodeConfig CodeConfiglist = getCodeConfigInfo(configID);//获取流水号
        if (CodeConfiglist!=null && (CodeConfiglist.getConfigcode())!=null){//若流水号不为空
            codeConfig = CodeConfiglist.getConfigcode();//根据编码规则实体类获取编码规则
            //系统变量生成 -年月日
            java.util.Date now = new java.util.Date();
            codeConfig = codeConfig.replace("[YY]", formatdate(now,"yy"));
            codeConfig = codeConfig.replace("[YYYY]", formatdate(now,"yyyy"));
            codeConfig = codeConfig.replace("[YYYYMM]",formatdate(now,"yyyyMM"));
            codeConfig = codeConfig.replace("[YYYYMMDD]",formatdate(now,"yyyyMMdd"));
            String strSerialCount = "0"; //当CodeConfig中未配置流水号的位数，则默认为0位
            if (codeConfig.contains("[XXXXX]")){
                strSerialCount = "00000";
                codeConfig = codeConfig.replace("[XXXXX]", "");
            }
            else if (codeConfig.contains("[XXXX]")){
                strSerialCount = "0000";
                codeConfig = codeConfig.replace("[XXXX]", "");
            }
            else if (codeConfig.contains("[XXX]")){
                strSerialCount = "000";
                codeConfig = codeConfig.replace("[XXX]", "");
            }
            else if (codeConfig.contains("[XX]")){
                strSerialCount = "00";
                codeConfig = codeConfig.replace("[XX]", "");
            }
            else if (codeConfig.contains("[X]")){
                strSerialCount = "0";
                codeConfig = codeConfig.replace("[X]", "");
            }
            //在获取流水号时，只能在执行参变量生成之前
            else if (codeConfig.contains("-")){
                strSerialCount = codeConfig.substring(codeConfig.indexOf("-") + 1);
                codeConfig = codeConfig.substring(0, codeConfig.indexOf("-"));
            }

            String codetypestr2= codeConfig+configID;
            List<CodeSerial>  list=getFromCodeSerial(codetypestr2);
            return updateCodeSerial(list,codetypestr2,strSerialCount,codeConfig);
        }
        return codeConfig;
    }
    public static String formatdate(Date dt, String format){
        String temp;
        if (dt == null){
            return "";
        }
        if ((format == null) || (format.equals(""))){
            temp = "yyyy-MM-dd";
        } else{
            temp = format;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(temp);
        String s = formatter.format(dt);
        return s;
    }
    public  CodeConfig getCodeConfigInfo(int configID) {
        return jpaApi.withTransaction(em -> {
            CodeConfig codeConfig = em.find(CodeConfig.class, configID);
            return codeConfig;
        });
    }
    public String updateCodeSerial(List<CodeSerial>  list,String codetypestr,String strSerialCount,String codeConfig){
        if(list.size()==0){//没有则插入数据，并且将serial设置为2
            CodeSerial codeserial2=new CodeSerial();
            codeserial2.setCodeType(codetypestr);
            codeserial2.setSerial(2);
            save(codeserial2);
            DecimalFormat f = new DecimalFormat(strSerialCount);
            String str = f.format(1);
            return codeConfig + str;
        }else{//有则将serial更新为加1
            CodeSerial codeserial=new CodeSerial();
            int temp=list.get(0).getSerial();
            codeserial.setSerial(temp+1);
            codeserial.setCodeType(list.get(0).getCodeType());
            update(codeserial);
            DecimalFormat f = new DecimalFormat(strSerialCount);
            String str = f.format(temp);
            return codeConfig+str;
        }
    }

    List<CodeSerial>  getFromCodeSerial(String codetypestr2){
        String sql=" select c from CodeSerial c  where c.codeType='"+codetypestr2+"'";
        return jpaApi.withTransaction(codeSerialList-> codeSerialList.createQuery(sql, CodeSerial.class).getResultList());
    }


    public CompletionStage<Void> save(CodeSerial codeSerial){
        return wrap(em -> {
            em.persist(codeSerial);
            return null;
        });
    }
    //包装
    private CompletionStage<Void> wrap(Function<EntityManager, Void> fn){
        return supplyAsync(() -> jpaApi.withTransaction(fn), executionContext);
    }
    public CompletionStage<Void> update(CodeSerial codeSerial){
        return wrap(em -> {
            em.merge(codeSerial);
            return null;
        });
    }
}
