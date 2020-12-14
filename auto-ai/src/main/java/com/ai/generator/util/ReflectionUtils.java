package com.ai.generator.util;

import com.ai.generator.model.Tree;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 反射相关辅助方法
 * @author Louis
 * @date Aug 19, 2018
 */
public class ReflectionUtils {
	private static DecimalFormat df = new DecimalFormat("#.##");
	/***
	 * double 字符串格式化保留两位小数格式
	 * @param str
	 * @return
	 */
	public static String doubleToString(String str){
		boolean b = validateNumber(str);
		if(b){
			return df.format(Double.parseDouble(str));
		}
		return str ;
	}
	/**

	 * 判断是否是整数或者是小数

	 * @param str

	 * @return true：是，false不是

	 */

	public static boolean validateNumber(String str) {
		if(StringUtils.isEmpty(str)) {
			return false;
		}
		// 说明一下的是该正则只能识别4位小数；如果不限制小数位数的话，写成[+-]?[0-9]+(\\.[0-9]+)?就可以了
		return str.matches("[+-]?[0-9]+(\\.[0-9]+)?");
	}
	public static String[] stringToArray(String str){
		String[] arr = null;
		if(!StringUtils.isEmpty(str)){
			Pattern p = Pattern.compile("\\s+");
			Matcher m = p.matcher(str);
			str= m.replaceAll(" ");
			arr = str.split(" ");
		}
		return arr ;
	}

	/**
	 * 把列表转化为tree形式
	 * @param list
	 */
	 public static void findTree( List list) {
		 Map<Long, Tree> tAreaMap = new HashMap<>();
		 Map<Long,List<Tree>> childMap = new HashMap<>();
		 if(list!=null && list.size()>0){
			 Iterator<Object> iterator = list.iterator();
			 while(iterator.hasNext()){
			 	 Object object = iterator.next() ;
			 	 if(!(object instanceof Tree)){
			 	 	break ;
				 }
				 Tree tree = (Tree)object;
				 if (tree.getParentId() == null || tree.getParentId().longValue() == 0) {
					 //根节点
					 //检查是否存在下级节点
					 findChildren(childMap,tree);
					 tAreaMap.put(tree.getId(),tree);
				 }else{
					 //子节点 则需要找到父节点
					 Tree parent = tAreaMap.get(tree.getParentId()) ;
					 if(parent!=null){
						 //如果含有 则表明是二级目录 或者三级目录
						 List<Tree> childrenList = parent.getChildren() ;
						 if(childrenList==null){
							 childrenList = new ArrayList<>() ;
							 parent.setChildren(childrenList);
						 }
						 tree.setParentName(parent.getName());
						 childrenList.add(tree);
						 //检查是否存在下级节点
						 findChildren(childMap,tree);
						 //存储到临时map中用于下级节点检索查找使用
						 tAreaMap.put(tree.getId(),tree);
					 }else{
						 //如果在临时map中未查找到parentId的区域 则表示三级区域先找到了 先保存下来
						 List<Tree>  childList = childMap.get(tree.getParentId());
						 if(childList == null){
							 childList = new ArrayList<>();
							 childMap.put(tree.getParentId(),childList) ;
						 }
						 childList.add(tree) ;
					 }
					 //非根目录 从当前列表中移除
					 iterator.remove();
				 }
			 }
		 }
		 tAreaMap.clear();
		 childMap.clear();
		 childMap = null ;
		 tAreaMap = null ;
	}

	private static  void findChildren(Map<Long,List<Tree>> childMap,Tree tree ){
		if(childMap.containsKey(tree.getId())){
			//存在则需要设置 然后移除临时child map中的列表数据
			List<Tree> childList = childMap.remove(tree.getId());
			if(childList!=null && childList.size()>0){
				for(Tree child:childList){
					child.setParentName(tree.getName());
				}
			}
			tree.setChildren(childList);
		}
	}
	
	/**
	 * 根据方法名调用指定对象的方法
	 * @param object 要调用方法的对象
	 * @param method 要调用的方法名
	 * @param args 参数对象数组
	 * @return
	 */
	public static Object invoke(Object object, String method, Object... args) {
		Object result = null;
		Class<? extends Object> clazz = object.getClass();
		Method queryMethod = getMethod(clazz, method, args);
		if(queryMethod != null) {
			try {
				result = queryMethod.invoke(object, args);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		} else {
			try {
				throw new NoSuchMethodException(clazz.getName() + " 类中没有找到 " + method + " 方法。");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 根据方法名和参数对象查找方法
	 * @param clazz
	 * @param name
	 * @param args 参数实例数据
	 * @return
	 */
	public static Method getMethod(Class<? extends Object> clazz, String name, Object[] args) {
		Method queryMethod = null;
		Method[] methods = clazz.getMethods();
		for(Method method:methods) {
			if(method.getName().equals(name)) {
				Class<?>[] parameterTypes = method.getParameterTypes();
				if(parameterTypes.length == args.length) {
					boolean isSameMethod = true;
					for(int i=0; i<parameterTypes.length; i++) {
						Object arg = args[i];
						if(arg == null) {
							arg = "";
						}
						if(!parameterTypes[i].equals(args[i].getClass())) {
							isSameMethod = false;
						}
					}
					if(isSameMethod) {
						queryMethod = method;
						break ;
					}
				}
			}
		}
		return queryMethod;
	}


	public static void clear(List list){
		if(list!=null){
			list.clear();
			list = null ;
		}
	}

	public static void clear(Map map){
		if(map!=null){
			map.clear();
			map = null ;
		}
	}
}
