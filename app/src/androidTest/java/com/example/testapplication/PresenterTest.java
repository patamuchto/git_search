package com.example.testapplication;

import android.support.test.runner.AndroidJUnit4;

import com.example.testapplication.main_screen.MainPresenter;
import com.example.testapplication.mocks.MockRepository;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class PresenterTest {
    @Test
    public void incompletePageTest() throws Exception {
        MockRepository repository = new MockRepository();
        MainPresenter presenter = new MainPresenter(repository);

        presenter.search("aaaaaa");
        Thread.sleep(1000);

        assertEquals(repository.getSmallerSize(),presenter.getCache().size());
        assertEquals(repository.getSmallerSize(),presenter.getIncompletePage().size());
        assertEquals(presenter.isHasMore(),true);

        presenter.loadMore();
        Thread.sleep(1000);

        assertEquals(repository.getBiggerSize(), presenter.getCache().size());
        assertEquals(repository.getBiggerSize(), presenter.getIncompletePage().size());
        assertEquals(presenter.isHasMore(),true);

        presenter.loadMore();
        Thread.sleep(1000);

        assertEquals(presenter.getCache().size(),repository.getCompleteSize());
        assertEquals(null,presenter.getIncompletePage());
        assertEquals(presenter.isHasMore(),false);
    }
}