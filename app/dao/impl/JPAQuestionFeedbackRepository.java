package dao.impl;

import com.google.inject.Singleton;
import dao.QuestionFeedbackRepository;
import models.QuestionFeedback;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPAQuestionFeedbackRepository implements QuestionFeedbackRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAQuestionFeedbackRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<List<QuestionFeedback>> findAll() {
        return supplyAsync(()->{
            return  jpaApi.withTransaction(questionList->{
                return questionList.createQuery("select p from QuestionFeedback p", QuestionFeedback.class).getResultList();
            });
        });
    }

    QuestionFeedback _findById(Integer id){
        return jpaApi.withTransaction(em -> {
            QuestionFeedback tfb = em.find(QuestionFeedback.class, id);
            return tfb;
        });
    }

    @Override
    public CompletionStage<QuestionFeedback> findById(Integer id){
        return supplyAsync(() -> {
            return _findById(id);
        });
    }

    QuestionFeedback findByCode(Integer code){
        return jpaApi.withTransaction(em -> {
            QuestionFeedback tfb = em.createQuery("select q from QuestionFeedback q where q.questionCode = ?1", QuestionFeedback.class)
                    .setParameter(1, code).getSingleResult();
            return tfb;
        });
    }


    private CompletionStage<Void> wrap(Function<EntityManager, Void> fn){
        return supplyAsync(() -> {
            return jpaApi.withTransaction(em ->{
                return fn.apply(em);
            });
        });
    }

    public CompletionStage<Void> save(QuestionFeedback fb){
        return wrap(em -> {
            em.persist(fb);
            return null;
        });
    }

    public CompletionStage<Void> update(QuestionFeedback fb){
        return wrap(em -> {
            em.merge(fb);
            return null;
        });
    }

    public CompletionStage<Void> updateNotNull(QuestionFeedback fb){
        return wrap(em ->{

            if(fb.getQuestionId() != null){
                QuestionFeedback tfb = em.find(QuestionFeedback.class, fb.getQuestionId());
                syncQuestion(fb, tfb);

            }else{
                QuestionFeedback tfb = em.createQuery("select q from QuestionFeedback q where q.questionCode = ?1", QuestionFeedback.class)
                        .setParameter(1, fb.getQuestionCode())
                        .getSingleResult();
                syncQuestion(fb, tfb);
            }

            return null;
        });
    }

    void syncQuestion(QuestionFeedback fb, QuestionFeedback tfb){
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
        


    }

}
