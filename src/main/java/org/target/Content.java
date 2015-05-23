package org.target;

import com.google.common.base.Charsets;
import com.google.common.hash.PrimitiveSink;

public class Content<T> {
	public class Funnel implements com.google.common.hash.Funnel<Content<T>> {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4354370185839330195L;

		@Override
		public void funnel(Content<T> from, PrimitiveSink into) {
			into.putString(name, Charsets.UTF_8)
			.putLong(contentId);
		}

	}

	private final String name;
	private final String description;
	private final Long contentId;
	private final T content;
	
	public Content(String name, String description, Long contentId,
			T content) {
		this.name = name;
		this.description = description;
		this.contentId = contentId;
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public Long getContentId() {
		return contentId;
	}

	public T getContent() {
		return content;
	}
}
