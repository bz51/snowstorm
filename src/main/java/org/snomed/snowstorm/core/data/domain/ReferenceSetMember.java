package org.snomed.snowstorm.core.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import org.snomed.snowstorm.rest.View;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

@Document(indexName = "es-member", type = "member", shards = 8)
public class ReferenceSetMember<C extends ReferenceSetMember> extends SnomedComponent<C> {

	public interface Fields extends SnomedComponent.Fields {
		String MEMBER_ID = "memberId";
		String REFSET_ID = "refsetId";
		String CONCEPT_ID = "conceptId";
		String REFERENCED_COMPONENT_ID = "referencedComponentId";
	}

	public interface LanguageFields {
		String ACCEPTABILITY_ID = "acceptabilityId";
	}

	@JsonView(value = View.Component.class)
	@Field(type = FieldType.keyword)
	private String memberId;

	@JsonView(value = View.Component.class)
	@Field(type = FieldType.keyword)
	private String moduleId;

	@JsonView(value = View.Component.class)
	@Field(type = FieldType.keyword)
	private String refsetId;

	@JsonView(value = View.Component.class)
	@Field(type = FieldType.keyword, store = true)
	private String referencedComponentId;

	// Used when the referencedComponentId is a description (or later possibly a relationship, depending how we implement concrete domains)
	@Field(type = FieldType.keyword, store = true)
	private String conceptId;

	@JsonView(value = View.Component.class)
	@Field(type = FieldType.Object)
	private Map<String, String> additionalFields;

	public ReferenceSetMember() {
		additionalFields = new HashMap<>();
	}

	public ReferenceSetMember(String memberId, String effectiveTime, boolean active, String moduleId, String refsetId,
			String referencedComponentId) {
		this();
		this.memberId = memberId;
		setEffectiveTime(effectiveTime);
		this.active = active;
		this.moduleId = moduleId;
		this.refsetId = refsetId;
		this.referencedComponentId = referencedComponentId;
	}

	public ReferenceSetMember(String moduleId, String refsetId, String referencedComponentId) {
		this(UUID.randomUUID().toString(), null, true, moduleId, refsetId, referencedComponentId);
	}

	@Override
	public boolean isComponentChanged(C that) {
		return that == null
				|| active != that.isActive()
				|| !moduleId.equals(that.getModuleId())
				|| !additionalFields.equals(that.getAdditionalFields());
	}

	@Override
	protected Object[] getReleaseHashObjects() {
		// TODO: This should probably include all additional fields
		Object[] hashObjects = new Object[2 + (additionalFields.size() * 2)];
		hashObjects[0] = active;
		hashObjects[1] = moduleId;
		int a = 2;
		for (String key : new TreeSet<>(additionalFields.keySet())) {
			hashObjects[a++] = key;
			hashObjects[a++] = additionalFields.get(key);
		}
		return hashObjects;
	}

	public String getAdditionalField(String fieldName) {
		return getAdditionalFields().get(fieldName);
	}

	public ReferenceSetMember<C> setAdditionalField(String fieldName, String value) {
		getAdditionalFields().put(fieldName, value);
		return this;
	}


	@Override
	@JsonIgnore
	public String getId() {
		return getMemberId();
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getModuleId() {
		return moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	public String getRefsetId() {
		return refsetId;
	}

	public void setRefsetId(String refsetId) {
		this.refsetId = refsetId;
	}

	public String getReferencedComponentId() {
		return referencedComponentId;
	}

	public ReferenceSetMember<C> setReferencedComponentId(String referencedComponentId) {
		this.referencedComponentId = referencedComponentId;
		return this;
	}

	public String getConceptId() {
		return conceptId;
	}

	public ReferenceSetMember<C> setConceptId(String conceptId) {
		this.conceptId = conceptId;
		return this;
	}

	public Map<String, String> getAdditionalFields() {
		return additionalFields;
	}

	public void setAdditionalFields(Map<String, String> additionalFields) {
		this.additionalFields = additionalFields;
	}

	@Override
	public String toString() {
		return "ReferenceSetMember{" +
				"memberId='" + memberId + '\'' +
				", effectiveTime='" + getEffectiveTime() + '\'' +
				", active=" + active +
				", moduleId='" + moduleId + '\'' +
				", refsetId='" + refsetId + '\'' +
				", referencedComponentId='" + referencedComponentId + '\'' +
				", additionalFields='" + additionalFields + '\'' +
				", conceptId='" + conceptId + '\'' +
				", internalId='" + getInternalId() + '\'' +
				", start='" + getStart() + '\'' +
				", end='" + getEnd() + '\'' +
				", path='" + getPath() + '\'' +
				'}';
	}

}