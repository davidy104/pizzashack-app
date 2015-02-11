package nz.co.pizzashack.model;

import java.io.InputStream;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class S3Asset {
	private String bucketName;
	private String key;
	private InputStream content;
	private long size;

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public InputStream getContent() {
		return content;
	}

	public void setContent(InputStream content) {
		this.content = content;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public static class Builder {
		private String bucketName;
		private String key;
		private InputStream content;
		private long size;

		public Builder bucketName(String bucketName) {
			this.bucketName = bucketName;
			return this;
		}

		public Builder size(long size) {
			this.size = size;
			return this;
		}

		public Builder content(InputStream content) {
			this.content = content;
			return this;
		}

		public Builder key(String key) {
			this.key = key;
			return this;
		}

		public S3Asset build() {
			S3Asset asset = new S3Asset();
			asset.setBucketName(bucketName);
			asset.setContent(content);
			asset.setKey(key);
			asset.setSize(size);
			return asset;
		}
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.bucketName, ((S3Asset) obj).bucketName)
				.append(this.key, ((S3Asset) obj).key)
				.append(this.size, ((S3Asset) obj).size)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder
				.append(this.bucketName)
				.append(this.key)
				.append(this.size)
				.toHashCode();

	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
				.append("bucketName", bucketName)
				.append("key", key)
				.append("size", size)
				.toString();
	}
}
