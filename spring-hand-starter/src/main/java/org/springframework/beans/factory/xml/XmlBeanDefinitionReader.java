package org.springframework.beans.factory.xml;

import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.support.BeanDefinitionRegistry;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;


/**
 *
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {


    private EntityResolver entityResolver;


    // TODO private ProblemReporter problemReporter = new FailFastProblemReporter();

    // TODO private ReaderEventListener eventListener = new EmptyReaderEventListener();

    // TODO private SourceExtractor sourceExtractor = new NullSourceExtractor();

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    // 我将档案馆传进来，档案馆也是一个注册器
    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    private final ThreadLocal<Set<EncodedResource>> resourcesCurrentlyBeingLoaded =
            new NamedThreadLocal<Set<EncodedResource>>("XML bean definition resources currently being loaded") {
                @Override
                protected Set<EncodedResource> initialValue() {
                    return new HashSet<>(4);
                }
            };

    // 装饰模式

    /**
     * 加载Bean定义信息
     */
    @Override
    public int loadBeanDefinitions(Resource resource) {
        return loadBeanDefinitions(new EncodedResource(resource));
    }

    public int loadBeanDefinitions(EncodedResource encodedResource) {

        Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();

        try (InputStream inputStream = encodedResource.getResource().getInputStream()) {
            InputSource inputSource = new InputSource(inputStream);
            if (encodedResource.getEncoding() != null) {
                inputSource.setEncoding(encodedResource.getEncoding());
            }
            //这个方法是我们关注的重点！！！！
            return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //TODO finally删除一些缓存
    }

    protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource) {
        // 将资源转换为Document方便后续处理，这里返回一个null不做实现
        Document doc = doLoadDocument(inputSource, resource);
        //下面是关键真正注册Bean定义信息在这里
        int count = registerBeanDefinitions(doc, resource);
        return count;


    }

    protected Document doLoadDocument(InputSource inputSource, Resource resource) {
        return null;
    }

    /**
     * 注册Bean定义信息
     */
    public int registerBeanDefinitions(Document doc, Resource resource) {
        BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
        int countBefore = getRegistry().getBeanDefinitionCount();
        // 就在这里是关键，把XmlReaderContext创建并且把档案馆赋值给了上下文，从而方便后期真正解析后将值赋给档案馆

        //这里我没有给Default的实现，在Default里的实现中会利用一个解析器来解析xml的标签，就可以解析成一个个BeanDefinition，封装成
        //BeanDefinitionHolder后，通过BeanDefinitionReaderUtils工具类，传入这个档案馆，利用档案馆的registerBeanDefinition方法注册BeanDefinition
        documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
        return getRegistry().getBeanDefinitionCount() - countBefore;
    }

    //不做实际实现
    private BeanDefinitionDocumentReader createBeanDefinitionDocumentReader() {
        return null;
    }

    public XmlReaderContext createReaderContext(Resource resource) {
        return null;
        //return new XmlReaderContext(resource, this.problemReporter, this.eventListener,
        //        this.sourceExtractor, this, getNamespaceHandlerResolver());
    }

    public Object getNamespaceHandlerResolver() {
        return null;
    }


}
