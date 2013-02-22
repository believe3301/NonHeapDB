package org.nonheapdb;

import java.util.Comparator;
import java.util.Objects;

public class RecordIndex implements Comparable<RecordIndex>, Comparator<RecordIndex> {
	
	
	private short index; 	// memory block index
	private int offset; 	// in memory block offset, record alignment?
	private int capacity; 	// allocated size

	public RecordIndex() {
		
	}
	
	public RecordIndex(long bucket) {
		this.capacity = (int)(bucket >>> 48);
		this.index = (short)((bucket >>> 32) & 0x0000FFFF);
		this.offset = (int)(bucket & 0x0000FFFFFFFFFFFFL);
	}
	
	public RecordIndex setIndex(int idx) {
		this.index =(short)idx;
		return this;
	}
	
	public RecordIndex setCapacity(int capacity) {
		this.capacity = capacity;
		return this;
	}
	
	public RecordIndex setOffset(int offset) {
		this.offset = offset;
		return this;
	}

	public short index() {
		return this.index;
	}

	public int offset() {
		return this.offset;
	}

	public int capacity() {
		return this.capacity;
	}

	//sort record by size
	public int compare(RecordIndex o1, RecordIndex o2) {
		return o1.compareTo(o2);
	}

	public int compareTo(RecordIndex rec) {
		//TODO using ComparisonChain
		int ret = this.capacity - rec.capacity;
		if (ret == 0) {
			return this.offset - rec.offset;
		}
		return ret;
	}
	
	//sort record by offset
	public static  Comparator<Long> offsetComparator() {
		return  new  Comparator<Long>() {
			public int compare(Long l, Long r) {
				RecordIndex lidx = new RecordIndex(l.longValue());
				RecordIndex ridx = new RecordIndex(r.longValue());
            	return lidx.offset - ridx.offset;
            }
		};
	}
	
	//sort record by capacity
	public static Comparator<Long> capacityComparator() {
		return  new  Comparator<Long>() {
			public int compare(Long l, Long r) {
				RecordIndex lidx = new RecordIndex(l.longValue());
				RecordIndex ridx = new RecordIndex(r.longValue());
            	return lidx.compareTo(ridx);
            }
		};
	}
	
	public int hashCode() {
		return  Objects.hash(index, offset, capacity);
	}
	
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		
		RecordIndex rec = (RecordIndex)obj;
		return this.index == rec.index &&
			   this.offset == rec.offset &&
			   this.capacity == rec.capacity;
	}
	
	public long getBucket() {
		return (long)capacity << 48 | (long)index << 32 | offset;
	}
	
	public String toString() {
		//TODO using Objects.toStringHelper
		StringBuilder sb = new StringBuilder();
		
		sb.append(String.format("index: %d\r\n", this.index));
		sb.append(String.format("offset: %d\r\n", this.offset));
		sb.append(String.format("capacity: %d\r\n", this.capacity));
		return sb.toString();
	}
}
