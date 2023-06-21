package top.whalefall.summerframework.context.support;

import top.whalefall.summerframework.beans.BeansException;
import top.whalefall.summerframework.beans.factory.config.BeanFactoryPostProcessor;
import top.whalefall.summerframework.beans.factory.config.BeanPostProcessor;
import top.whalefall.summerframework.beans.factory.support.AbstractAutowireCapableBeanFactory;
import top.whalefall.summerframework.context.ApplicationContext;
import top.whalefall.summerframework.core.io.DefaultResourceLoader;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ApplicationContext {
    /**
     * 模范方法
     */
    @Override
    public void refresh() {
        // 1. 创建 BeanFactory, 并加载 BeanDefinition
        refreshBeanFactory();
        // 2, 获取 BeanFactory
        AbstractAutowireCapableBeanFactory beanFactory = getBeanFactory();
        // 3. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor (Invoke factory processors registered as beans in the context.)
        invokeBeanFactoryPostProcessors(beanFactory);
        // 4. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);
        // 5. 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract AbstractAutowireCapableBeanFactory getBeanFactory();

    private void invokeBeanFactoryPostProcessors(AbstractAutowireCapableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(AbstractAutowireCapableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }

}
