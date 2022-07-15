package io.laokou.common.utils;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.google.common.collect.Lists;
import io.laokou.common.exception.CustomException;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
/**
 * 构造树
 * @author Kou Shenhai
 * @version 1.0
 * @date 2022/5/20 0020 下午 8:20
 */
@Data
public class TreeUtil<T> {
    public static <T extends TreeNo> TreeNo<T> rootRootNode(String name,String icon){
        return new TreeNo(0L,name,null,icon, Lists.newArrayList());
    }
    public static <T extends TreeNo> TreeNo<T> rootRootNode(String name) {
        return rootRootNode(name,"");
    }
    public static <T extends TreeNo> TreeNo<T> rootRootNode() {
        return rootRootNode("根节点");
    }
    public static <T extends TreeNo> T buildTreeNode(List<T> treeNodes,T rootNode) {
        if (null == rootNode) {
            throw new CustomException("请构造根节点");
        }
        treeNodes.add(rootNode);
        //list转map
        Map<Long, T> nodeMap = new LinkedHashMap<>(treeNodes.size());
        for (T treeNode : treeNodes) {
            nodeMap.put(treeNode.getId(), treeNode);
        }
        for (T treeNo : treeNodes) {
            T parent = nodeMap.get(treeNo.getPid());
            if (parent != null && treeNo.getPid().equals(parent.getId())) {
                if (parent.getChildren() == null) {
                    parent.setChildren(Lists.newArrayList());
                }
                parent.getChildren().add(treeNo);
            }
        }
        return rootNode;
    }
    public static <T extends TreeNo> TreeNo<T> findNode(Long id,List<TreeNo> nodeList) {
        for (TreeNo treeNo : nodeList) {
            if (CollectionUtils.isNotEmpty(treeNo.getChildren())) {
                findNode(id,treeNo.getChildren());
            }
            if (id.equals(treeNo.getId())) {
                return treeNo;
            }
        }
        return null;
    }
    public static class TreeNo<T> {
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long id;
        private String name;
        @JSONField(serializeUsing = ToStringSerializer.class)
        private Long pid;
        private String icon;
        private List<T> children;
        public TreeNo() {}
        public TreeNo(Long id, String name, Long pid, String icon, List<T> children) {
            this.id = id;
            this.name = name;
            this.pid = pid;
            this.icon = icon;
            this.children = children;
        }
        @Override
        public String toString() {
            return new StringJoiner(", ", TreeNo.class.getSimpleName() + "[", "]")
                    .add("id='" + id + "'")
                    .add("name='" + name + "'")
                    .add("pid='" + pid + "'")
                    .add("icon='" + icon + "'")
                    .add("children=" + children)
                    .toString();
        }
        public Long getId() {
            return id;
        }
        public void setId(Long id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public Long getPid() {
            return pid;
        }
        public void setPid(Long pid) {
            this.pid = pid;
        }
        public List<T> getChildren() {
            return children;
        }
        public void setChildren(List<T> children) {
            this.children = children;
        }
        public String getIcon() {
            return icon;
        }
        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
