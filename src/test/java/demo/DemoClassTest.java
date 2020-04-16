package demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoClassTest {

    @Mock
    private Calculator calculator;

    @BeforeEach
    void setUp() {
        when(calculator.add(1, 2)).thenReturn(3);
    }

    @Test
    void test() {
        //given
        int a = 1;
        int b = 2;

        //when
        int result = calculator.add(a, b);

        //than
        assertThat(result).isEqualTo(3);
    }
}
