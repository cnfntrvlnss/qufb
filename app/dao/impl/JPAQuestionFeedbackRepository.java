package dao.impl;

import com.google.inject.Singleton;
import dao.QuestionFeedbackRepository;
import models.QuestionFeedback;
import models.viewModels.QuestionStateEnum;
import play.Logger;
import play.db.jpa.JPAApi;
import scala.collection.immutable.Page;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.awt.print.Pageable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAQuestionFeedbackRepository implements QuestionFeedbackRepository {
    private final Logger.ALogger logger = Logger.of(JPAQuestionFeedbackRepository.class);

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAQuestionFeedbackRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    /**
     * 获取所有的问题反馈列表
     * @return
     */
    @Override
    public CompletionStage<List<QuestionFeedback>> findAll() {
        return supplyAsync(()-> jpaApi.withTransaction(questionList-> questionList.createQuery("select p from QuestionFeedback p order by p.feedbackTime desc", QuestionFeedback.class).getResultList()));
    }

    /**
     * 带条件的查询语句
     * lixin
     * 2018-9-7 09:10:42
     * @param userName
     * @return
     */
    @Override
    public CompletionStage<List<QuestionFeedback>> findAll(String userName) {
        return supplyAsync(()-> jpaApi.withTransaction(questionList-> questionList.createQuery("select p from QuestionFeedback p where p.feedbacker=?1 or p.bugHeader=?1 or p.transferName =?1 or p.developerName =?1 or p.schemeAuditName=?1 or p.resultAuditName=?1 or p.verifyName =?1   order by p.feedbackTime desc", QuestionFeedback.class) .setParameter(1, userName).getResultList()));
    }

    /**
     *
     * @param questionFeedback
     * @return
     */
    @Override
    public Page<QuestionFeedback> getQuestionPagingList(QuestionFeedback questionFeedback, Integer page, Integer size) {
       /* Pageable pageable=PageRequest.of(page-1,size);
        Page<QuestionFeedback> questionPage
   */             return null;



    }

    @Override
    public CompletionStage<List<QuestionFeedback>> findAll(QuestionFeedback questionFeedback,String userName) {
        String sql="select q from QuestionFeedback q    where 1=1 and (q.feedbacker=?1 or q.bugHeader=?1 or q.transferName =?1 or q.developerName =?1 or q.schemeAuditName=?1 or q.resultAuditName=?1 or q.verifyName =?1)   " ;
                if(questionFeedback.getQuestionTitle()!=null){
                    sql+=" and q.questionTitle like '%"+questionFeedback.getQuestionTitle()+"%'";
                }
                if(questionFeedback.getBugHeader()!=null){
                    sql+=" and q.bugHeader like '%"+questionFeedback.getBugHeader()+"%'";
                }
                if(questionFeedback.getFeedbacker()!=null){
                    sql+=" and q.feedbacker like '%"+questionFeedback.getFeedbacker()+"%'";
                }
                if(questionFeedback.getTransferName()!=null){
                    sql+=" and q.transferName like '%"+questionFeedback.getTransferName()+"%'";
                }
                if(questionFeedback.getDeveloperName()!=null){
                    sql+=" and q.developerName like '%"+questionFeedback.getDeveloperName()+"%'";
                }
                if(questionFeedback.getSchemeAuditName()!=null){
                    sql+=" and q.schemeAuditNamelike '%"+questionFeedback.getSchemeAuditName()+"%'";
                }
                if(questionFeedback.getVerifyName()!=null){
                    sql+=" and q.verifyName like '%"+questionFeedback.getVerifyName()+"%'";
                }


               sql += " order by q.feedbackTime desc";
                String sqlQuery=sql;
        logger.debug("{}", sqlQuery);
        return supplyAsync(()-> jpaApi.withTransaction(questionList-> questionList.createQuery(sqlQuery, QuestionFeedback.class) .setParameter(1, userName).getResultList()));
    }

    /**
     * 通过id获取一个问题反馈的实体信息
     * @param id
     * @return
     */
    @Override
    public CompletionStage<QuestionFeedback> findById(Integer id){
        return supplyAsync(() -> _findById(id));
    }

    /**
     * 通过问题编号查询一个问题实体
     * @param code
     * @return
     */
    QuestionFeedback findByCode(String code){
        return jpaApi.withTransaction(em -> {
            QuestionFeedback tfb = em.createQuery("select q from QuestionFeedback q where q.questionCode = ?1", QuestionFeedback.class)
                    .setParameter(1, code).getSingleResult();
            return tfb;
        });
    }
    /**
     * 保存一个问题
     * @param fb
     * @return
     */
    public CompletionStage<Void> save(QuestionFeedback fb){
        return wrap(em -> {
            em.persist(fb);
            return null;
        });
    }

    /**
     * 更新一个问题实体
     * @param fb
     * @return
     */
    public CompletionStage<Void> update(QuestionFeedback fb){
        return wrap(em -> {
            em.merge(fb);
            return null;
        });
    }

    /**
     * 通过问题id或问题编号更新一个问题，包括条件为空的判断
     * @param fb
     * @return
     */
    public CompletionStage<Void> updateNotNull(QuestionFeedback fb){
        return wrap(em ->{
            if(fb.getQuestionId() != null){
                QuestionFeedback tfb = em.find(QuestionFeedback.class, fb.getQuestionId());
                _syncQuestion(fb, tfb);

            }else if(fb.getQuestionCode()!=null){
                QuestionFeedback tfb =findByCode(fb.getQuestionCode());
                //QuestionFeedback tfb = em.createQuery("select q from QuestionFeedback q where q.questionCode = ?1", QuestionFeedback.class).setParameter(1, code).getSingleResult().getSingleResult();
                _syncQuestion(fb, tfb);
            }
            return null;
        });
    }

    //==============================================以下为私有方法

   //通过id获取一个问题实体并返回实体
    private QuestionFeedback _findById(Integer id){
        return jpaApi.withTransaction(em -> {
            QuestionFeedback tfb = em.find(QuestionFeedback.class, id);
            return tfb;
        });
    }
    //包装
    private CompletionStage<Void> wrap(Function<EntityManager, Void> fn){
        return supplyAsync(() -> jpaApi.withTransaction(fn), executionContext);
    }

    /**
     *
     * @param fb：要更新的实体
     * @param tfb：数据库中目前的实体信息
     */
   private  void _syncQuestion(QuestionFeedback fb, QuestionFeedback tfb) {
        try{
            Class<QuestionFeedback> clz = QuestionFeedback.class;
            for(Field f: Arrays.asList(clz.getDeclaredFields())){
                String fname = f.getName();
                if(fname.equals("questionId") || fname.equals("questionCode")){
                    continue;
                }
                String mPart = fname.substring(0, 1).toUpperCase() + fname.substring(1);
                Method m = clz.getMethod("get" + mPart);
                Object fromGet = m.invoke(fb);
                if(fromGet != null){
                    Method mset = clz.getMethod("set" + mPart, fromGet.getClass());
                    logger.debug("in _syncQuestion, {}({})", mset.getName(), fromGet);
                    mset.invoke(tfb, fromGet);
                }
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    void syncQuestion(QuestionFeedback fb, QuestionFeedback tfb) {
        if(fb.getQuestionTitle() != null){
            tfb.setQuestionTitle(fb.getQuestionTitle());
        }
        if(fb.getFeedbacker() != null){
            tfb.setFeedbacker(fb.getFeedbacker());
        }
        if(fb.getFeedbackerId() != null){
            tfb.setFeedbackerId(fb.getFeedbackerId());
        }
        if(fb.getFeedbackTime() != null){
            tfb.setFeedbackTime(fb.getFeedbackTime());
        }
        if(fb.getFeedbackSuggestion() != null){
            tfb.setFeedbackSuggestion(fb.getFeedbackSuggestion());
        }
        if(fb.getBugHeader() != null){
            tfb.setBugHeader(fb.getBugHeader());
        }
        if(fb.getBugHeaderId() != null){
            tfb.setBugHeaderId(fb.getBugHeaderId());
        }
        if(fb.getAuditSuggestion() != null){
            tfb.setAuditSuggestion(fb.getAuditSuggestion());
        }
        if(fb.getAuditTime() != null){
            tfb.setAuditTime(fb.getAuditTime());
        }
        if(fb.getTransferName() != null){
            tfb.setTransferName(fb.getTransferName());
        }
        if(fb.getTransferId() != null){
            tfb.setTransferId(fb.getTransferId());
        }
        if(fb.getTransferSuggestion() != null){
            tfb.setTransferSuggestion(fb.getTransferSuggestion());
        }
        if(fb.getTransferTime() != null){
            tfb.setTransferTime(fb.getTransferTime());
        }
        if(fb.getDeveloperName() != null){
            tfb.setDeveloperName(fb.getDeveloperName());
        }
        if(fb.getDeveloperId() != null){
            tfb.setDeveloperId(fb.getDeveloperId());
        }
        if(fb.getSolution() != null){
            tfb.setSolution(fb.getSolution());
        }
        if(fb.getSoluteTime() != null){
            tfb.setSoluteTime(fb.getSoluteTime());
        }
        if(fb.getSchemeAuditName() != null){
            tfb.setSchemeAuditName(fb.getSchemeAuditName());
        }
        if(fb.getSchemeAuditId() != null){
            tfb.setSchemeAuditId(fb.getSchemeAuditId());
        }
        if(fb.getSchemeAuditSuggestion() != null){
            tfb.setSchemeAuditSuggestion(fb.getSchemeAuditSuggestion());
        }
        if(fb.getSchemeAuditTime() != null){
            tfb.setSchemeAuditTime(fb.getSchemeAuditTime());
        }
        if(fb.getResultAuditName() != null){
            tfb.setResultAuditName(fb.getResultAuditName());
        }
        if(fb.getResultAuditId() != null){
            tfb.setResultAuditId(fb.getResultAuditId());
        }
        if(fb.getResultAuditSuggestion() != null){
            tfb.setResultAuditSuggestion(fb.getResultAuditSuggestion());
        }
        if(fb.getResultAuditTime() != null){
            tfb.setResultAuditTime(fb.getResultAuditTime());
        }
        if(fb.getVerifyName() != null){
            tfb.setVerifyName(fb.getVerifyName());
        }
        if(fb.getVerifyId() != null){
            tfb.setVerifyId(fb.getVerifyId());
        }
        if(fb.getVerifySuggestion() != null){
            tfb.setVerifySuggestion(fb.getVerifySuggestion());
        }
        if(fb.getVerifyTime() != null){
            tfb.setVerifyTime(fb.getVerifyTime());
        }
        if(fb.getEstimateFinishTime() != null){
            tfb.setEstimateFinishTime(fb.getEstimateFinishTime());
        }
        if(fb.getLatestUpdateTime() != null){
            tfb.setLatestUpdateTime(fb.getLatestUpdateTime());
        }
        if(fb.getProjectId() != null){
            tfb.setProjectId(fb.getProjectId());
        }
        if(fb.getProjectName() != null){
            tfb.setProjectName(fb.getProjectName());
        }
        if(fb.getQuestionDescription() != null){
            tfb.setQuestionDescription(fb.getQuestionDescription());
        }
        if(fb.getQuestionState() != null){
            tfb.setQuestionState(fb.getQuestionState());
        }
        if(fb.getQuestionType() != null){
            tfb.setQuestionType(fb.getQuestionType());
        }
        if(fb.getQuestionLevel() != null){
            tfb.setQuestionLevel(fb.getQuestionLevel());
        }
        if(fb.getFlowState() != null){
            tfb.setFlowState(fb.getFlowState());
        }

    }

}
