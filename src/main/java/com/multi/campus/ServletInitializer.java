package com.multi.campus;

import java.util.Collections;

import org.apache.catalina.Context;
import org.apache.tomcat.util.descriptor.web.JspConfigDescriptorImpl;
import org.apache.tomcat.util.descriptor.web.JspPropertyGroup;
import org.apache.tomcat.util.descriptor.web.JspPropertyGroupDescriptorImpl;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
//web.xml 역할을 수행함
@Component
public class ServletInitializer extends SpringBootServletInitializer{
	
	@Bean
	public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {

			@Override
			protected void postProcessContext(Context ctx) {
				super.postProcessContext(ctx);
				
				JspPropertyGroup group = new JspPropertyGroup();
				group.addUrlPattern("*.jsp");
				group.setPageEncoding("UTF-8");
				group.setScriptingInvalid("true");
				group.addIncludePrelude("/WEB-INF/views/inc/top.jspf");
				group.addIncludeCoda("/WEB-INF/views/inc/foot.jspf");
				group.setTrimWhitespace("true");
				group.setDefaultContentType("text/html");
				
				JspPropertyGroupDescriptorImpl jspPropertyGroupDescriptor = new JspPropertyGroupDescriptorImpl(
						group);
				ctx.setJspConfigDescriptor(new JspConfigDescriptorImpl(
						Collections.singletonList(jspPropertyGroupDescriptor), Collections.emptyList()));
				
			}
			
		};
		
		return factory;
	}
}
