//工具集合Tools
window.T = {};

// 获取请求参数
// 使用示例
// location.href = http://localhost:8080/index.html?id=123
// T.p('id') --> 123;
var url = function(name) {
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};
T.p = url;


(function() {
    ren = {
        selectUserDialog : function(token, func){
            top.layer.open({
                type: 2,
                area: ['800px', '500px'],
                title:"选择用户",
                auto:true,
                maxmin: true,
                content: "editor-app/renren-dialog/user.html?token=" + token,
                btn: ['确定', '关闭'],
                yes: function(index, data){
                    var ids = data.find("iframe")[0].contentWindow.getIds();

                    func(ids.join(","));

                    top.layer.close(index);
                },
                cancel: function(index){
                    top.layer.close(index);
                }
            });
        },
        selectRoleDialog : function(token, func){
            top.layer.open({
                type: 2,
                area: ['800px', '500px'],
                title:"选择角色",
                auto:true,
                maxmin: true,
                content: "editor-app/renren-dialog/role.html?token=" + token,
                btn: ['确定', '关闭'],
                yes: function(index, data){
                    var ids = data.find("iframe")[0].contentWindow.getIds();

                    func(ids.join(","));

                    top.layer.close(index);
                },
                cancel: function(index){
                    top.layer.close(index);
                }
            });
        }
    }
})(jQuery);