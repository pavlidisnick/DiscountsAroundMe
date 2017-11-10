package com.tl.discountsaroundme;

import android.app.Activity;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.tl.discountsaroundme.FirebaseData.DiscountsManager;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TypedValue.class, LayoutInflater.class, AddCategoryToLayout.class})
public class AddCategoryTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createObject() throws Exception {
        Activity activity = mock(Activity.class);
        LinearLayout linearLayout = mock(LinearLayout.class);

        PowerMockito.mockStatic(TypedValue.class);
        PowerMockito.spy(TypedValue.applyDimension(1, 28, null));

        Mockito.when(TypedValue.applyDimension(1, 28, null)).thenReturn((float) 100);
        when(activity.getResources()).thenReturn(mock(Resources.class));
        when(activity.getResources().getDisplayMetrics()).thenReturn(mock(DisplayMetrics.class));

        AddCategoryToLayout addCategoryToLayout = new AddCategoryToLayout(linearLayout, activity);

        AddCategoryToLayout addCategoryToLayout1 = PowerMockito.spy(addCategoryToLayout);
        PowerMockito.doNothing().when(addCategoryToLayout1, "addSpace");

        Button button = mock(Button.class);
        LayoutInflater layoutInflater = mock(LayoutInflater.class);
        when(layoutInflater.inflate(R.layout.button_category_tag, null, false)).thenReturn(button);
        PowerMockito.mockStatic(LayoutInflater.class);
        Mockito.when(LayoutInflater.from(activity)).thenReturn(layoutInflater);

        DiscountsManager mockDiscountManager = mock(DiscountsManager.class);
        addCategoryToLayout1.addCategory("ok", mockDiscountManager);
        assertNotNull(addCategoryToLayout);
    }
}