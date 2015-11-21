package org.hage.platform.config.transl;

import org.hage.platform.component.definition.IComponentDefinition;
import org.hage.platform.config.ComputationConfiguration;
import org.hage.platform.config.def.HabitatExternalConfiguration;
import org.hage.platform.config.def.HabitatInternalConfiguration;
import org.hage.platform.config.loader.Configuration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ConfigurationTranslatorTest {

    @InjectMocks
    private ConfigurationTranslator tested;
    @Mock
    private HabitatConfigurationTranslator habitatConfigurationTranslatorMock;


    @Test
    public void shouldTranslateToConfigurationWithSameGlobalComponents() throws Exception {

        // given

        IComponentDefinition firstComponentDefinition = mock(IComponentDefinition.class);
        IComponentDefinition secondComponentDefinition = mock(IComponentDefinition.class);

        final Configuration configuration = Configuration.builder().globalComponents(asList(
                firstComponentDefinition,
                secondComponentDefinition
        )).build();

        // when

        ComputationConfiguration translatedConfiguration = tested.translate(configuration);

        // then

        assertThat(translatedConfiguration.getGlobalComponents()).containsOnly(firstComponentDefinition, secondComponentDefinition);


    }

    @Test
    public void shouldTranslateToConfigurationAndUseHabitatConfigurationTranslator() throws Exception {

        // given

        final HabitatExternalConfiguration externalConf = mock(HabitatExternalConfiguration.class);
        final Configuration configuration = Configuration.builder().habitatConfiguration(externalConf).build();

        final HabitatInternalConfiguration expectedInternalConf = mock(HabitatInternalConfiguration.class);

        when(habitatConfigurationTranslatorMock.toInternalModel(externalConf)).thenReturn(expectedInternalConf);

        // when

        ComputationConfiguration translatedConf = tested.translate(configuration);

        // then

        assertThat(translatedConf.getHabitatConfiguration()).isSameAs(expectedInternalConf);


    }
}