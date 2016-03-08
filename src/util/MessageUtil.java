package util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageUtil implements MessageSourceAware
{
	private MessageSource messageSource;
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public String getMessage(String code, Object[] args) {
		if(args==null){
			args = new Object[]{};
		}
		Locale locale = LocaleContextHolder.getLocale();
		String message = messageSource.getMessage(code,args, locale);
    	return message;
	}
}