package ru.surfstudio.android.core.mvp.rx.domain

import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TwoWayTest : BaseRelationTest() {

    private lateinit var twoWay: TwoWay<String>

    private lateinit var testViewObservable: TestObserver<String>
    private lateinit var testViewConsumer: Consumer<String>

    private lateinit var testPresenterObservable: TestObserver<String>
    private lateinit var testPresenterConsumer: Consumer<String>

    @Before
    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        twoWay = TwoWay()

        testViewConsumer =
                with(testView) {
                    twoWay.getConsumer()
                }

        testViewObservable =
                with(testView) {
                    twoWay.getObservable().test()
                }

        testPresenterConsumer=
                with(testPresenter) {
                    twoWay.getConsumer()
                }

        testPresenterObservable =
                with(testPresenter) {
                    twoWay.getObservable().test()
                }
    }

    @Test
    @Throws(Exception::class)
    fun test() {
        assertNotEquals(testViewObservable, testPresenterObservable)
        assertNotEquals(testViewConsumer, testPresenterConsumer)

        testViewObservable
                .assertNoValues()
                .assertNoErrors()

        testPresenterObservable
                .assertNoValues()
                .assertNoErrors()

        assertFalse(twoWay.hasValue)

        testPresenterConsumer.accept("TEST_FROM_PRESENTER")
        testViewObservable
                .assertValueCount(1)

        assertTrue(twoWay.hasValue)

        assertEquals("TEST_FROM_PRESENTER", twoWay.value)

        testViewConsumer.accept("TEST_FROM_VIEW")
        testPresenterObservable
                .assertValueCount(1)

        assertTrue(twoWay.hasValue)

        assertEquals("TEST_FROM_VIEW", twoWay.value)
    }
}