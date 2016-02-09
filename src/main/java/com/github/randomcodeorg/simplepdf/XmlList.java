package com.github.randomcodeorg.simplepdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


class XmlList<T> implements XmlSerializable, List<T>{

	private String tagName;
	private List<T> data;
	
	
	public XmlList(String tagName, List<T> data){
		
		if(tagName == null) throw new NullPointerException("The tagName may not be null.");
		if(tagName.isEmpty())throw new IllegalArgumentException("The tagName may not be empty.");
		if(data == null) throw new NullPointerException("The data may not be null.");
		this.tagName = tagName;
		this.data = data;
	}
	
	public XmlList(String tagName, Iterable<T> data){
		
		if(tagName == null) throw new NullPointerException("The tagName may not be null.");
		if(tagName.isEmpty())throw new IllegalArgumentException("The tagName may not be empty.");
		if(data == null) throw new NullPointerException("The data may not be null.");
		this.tagName = tagName;
		this.data = toList(data);
	}
	
	public XmlList(String tagName, T[] data){
		
		if(tagName == null) throw new NullPointerException("The tagName may not be null.");
		if(tagName.isEmpty())throw new IllegalArgumentException("The tagName may not be empty.");
		if(data == null) throw new NullPointerException("The data may not be null.");
		this.tagName = tagName;
		this.data = Arrays.asList(data);
	}
	
	public XmlList(String tagName){
		
		if(tagName == null) throw new NullPointerException("The tagName may not be null.");
		if(tagName.isEmpty())throw new IllegalArgumentException("The tagName may not be empty.");
		this.tagName = tagName;
		data = new ArrayList<T>();
	}
	
	private List<T> toList(Iterable<T> data){
		ArrayList<T> result = new ArrayList<T>();
		for(T e:data){
			result.add(e);
		}
		return result;
	}
	
	
	public String getTagName(){
		return tagName;
	}
	
	public void setTagName(String tagName){
		if(tagName == null) throw new NullPointerException("The tagName may not be null.");
		if(tagName.isEmpty())throw new IllegalArgumentException("The tagName may not be empty.");
		this.tagName = tagName;
	}
	
	@Override
	public String toXML() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("<%s>\n", tagName));
		for(T o : data){
			try{
				XmlSerializable s = (XmlSerializable) o;
				sb.append(FormattingTools.indentText(s.toXML()));
				sb.append("\n");
			}catch(Throwable t){
				
			}
		}
		sb.append(String.format("</%s>", tagName));
		return sb.toString();
	}

	
	
	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return data.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return data.iterator();
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	

	@Override
	public boolean add(T e) {
		return data.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return data.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return data.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return data.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return data.addAll(index, c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return data.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return data.retainAll(c);
	}

	@Override
	public void clear() {
		data.clear();
	}

	@Override
	public T get(int index) {
		return data.get(index);
	}

	@Override
	public T set(int index, T element) {
		return data.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		 data.add(index, element);
	}

	@Override
	public T remove(int index) {
		return data.remove(index);
	}

	@Override
	public int indexOf(Object o) {
		return data.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return data.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return data.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return data.subList(fromIndex, toIndex);
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

}
