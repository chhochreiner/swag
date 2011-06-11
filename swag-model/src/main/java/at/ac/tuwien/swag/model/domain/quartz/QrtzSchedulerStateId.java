package at.ac.tuwien.swag.model.domain.quartz;

// Generated Jun 11, 2011 5:07:29 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * QrtzSchedulerStateId generated by hbm2java
 */
@Embeddable
public class QrtzSchedulerStateId implements java.io.Serializable {

	private String schedName;
	private String instanceName;

	public QrtzSchedulerStateId() {
	}

	public QrtzSchedulerStateId( String schedName, String instanceName ) {
		this.schedName = schedName;
		this.instanceName = instanceName;
	}

	@Column(name = "sched_name", nullable = false, length = 120)
	public String getSchedName() {
		return this.schedName;
	}

	public void setSchedName( String schedName ) {
		this.schedName = schedName;
	}

	@Column(name = "instance_name", nullable = false, length = 200)
	public String getInstanceName() {
		return this.instanceName;
	}

	public void setInstanceName( String instanceName ) {
		this.instanceName = instanceName;
	}

	public boolean equals( Object other ) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof QrtzSchedulerStateId))
			return false;
		QrtzSchedulerStateId castOther = (QrtzSchedulerStateId) other;

		return ((this.getSchedName() == castOther.getSchedName()) || (this
				.getSchedName() != null && castOther.getSchedName() != null && this
				.getSchedName().equals( castOther.getSchedName() )))
				&& ((this.getInstanceName() == castOther.getInstanceName()) || (this
						.getInstanceName() != null
						&& castOther.getInstanceName() != null && this
						.getInstanceName().equals( castOther.getInstanceName() )));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSchedName() == null ? 0 : this.getSchedName().hashCode());
		result = 37
				* result
				+ (getInstanceName() == null ? 0 : this.getInstanceName()
						.hashCode());
		return result;
	}

}