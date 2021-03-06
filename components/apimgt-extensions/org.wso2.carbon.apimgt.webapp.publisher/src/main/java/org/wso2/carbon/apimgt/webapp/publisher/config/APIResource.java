/*
 * Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * you may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.webapp.publisher.config;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Resource")
public class APIResource{

	private String AuthType;
	private String HttpVerb;
	private String Uri;
	private String UriTemplate;
	private String consumes;
	private String produces;

	public String getAuthType() {
		return AuthType;
	}

	@XmlElement(name = "AuthType", required = true)
	public void setAuthType(String authType) {
		AuthType = authType;
	}

	public String getHttpVerb() {
		return HttpVerb;
	}

	@XmlElement(name = "HttpVerb", required = true)
	public void setHttpVerb(String httpVerb) {
		HttpVerb = httpVerb;
	}

	public String getUri() {
		return Uri;
	}

	@XmlElement(name = "Uri", required = true)
	public void setUri(String uri) {
		Uri = uri;
	}

	public String getUriTemplate() {
		return UriTemplate;
	}

	@XmlElement(name = "UriTemplate", required = true)
	public void setUriTemplate(String uriTemplate) {
		UriTemplate = uriTemplate;
	}

	public String getConsumes() {
		return consumes;
	}

	@XmlElement(name = "Consumes", required = true)
	public void setConsumes(String consumes) {
		this.consumes = consumes;
	}

	public String getProduces() {
		return produces;
	}

	@XmlElement(name = "Produces", required = true)
	public void setProduces(String produces) {
		this.produces = produces;
	}
}
