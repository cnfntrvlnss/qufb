package dao;

import models.QuestionFeedback;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.test.WithApplication;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class QuestionFeedbackRepositoryTest extends WithApplication {
    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    @Test
    public void testGeneral(){
        final QuestionFeedbackRepository repo = app.injector().instanceOf(QuestionFeedbackRepository.class);

        QuestionFeedback fb = new QuestionFeedback();
        //生成一个唯一的code
        fb.setQuestionCode("QT" + Long.toString(new Date().getTime()));
        fb.setQuestionTitle("地址错误");
        repo.save(fb).toCompletableFuture().join();
        assertThat(fb.getQuestionId()).isNotNull();
        QuestionFeedback fb1 = new QuestionFeedback();
        fb1.setQuestionId(fb.getQuestionId());
        fb1.setQuestionTitle("姓名错误");
        repo.updateNotNull(fb1).toCompletableFuture().join();
        //fb不再受管
        assertThat(fb).extracting(QuestionFeedback::getQuestionTitle).containsExactly("地址错误");

        fb = repo.findById(fb.getQuestionId()).toCompletableFuture().join();
        assertThat(fb).extracting(QuestionFeedback::getQuestionTitle).containsExactly("姓名错误");
        Integer id = fb.getQuestionId();
        fb.setQuestionId(null);
        fb.setQuestionTitle("电话错误");
        repo.updateNotNull(fb).toCompletableFuture().join();
        fb = repo.findById(id).toCompletableFuture().join();
        assertThat(fb.getQuestionTitle()).isEqualTo("电话错误");

    }
}
