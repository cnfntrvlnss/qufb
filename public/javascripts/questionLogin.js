var Flag = false;
		Flag = IsPC();
		if(Flag){
			var isChrome = window.navigator.userAgent.indexOf("Chrome") !== -1;
			if(!isChrome) location.href ="../ie.jsp";
		}
		function IsPC() {
			    var userAgentInfo = navigator.userAgent;
			    var Agents = ["Android", "iPhone",
							                  "SymbianOS", "Windows Phone",
							                  "iPad", "iPod"];
			    var flag = true;
			    for (var v = 0; v < Agents.length; v++) {
				        if (userAgentInfo.indexOf(Agents[v]) > 0) {
					            flag = false;
					            break;
					        }
				    }
			    return flag;
		}
		var rrm = false;
        	$(document).ready(function(){
        		　　 noHis(); //用户退出后，防止用户点击后退按钮
        		readCookies();
        	});

        	function noHis(){
        		if (window.history && window.history.pushState) {
        			　　$(window).on('popstate', function () {
        				　　window.history.pushState('forward', null, '${systemUrl}');
        				　　window.history.forward(1);
        				　　	   });
        			　}
        		　window.history.pushState('forward', null, '${systemUrl}'); //在IE中必须得有这两行
        		　window.history.forward(1);
        	}

        	//读取cookies
        	function readCookies(){
        		var adname = $.cookie('adname');
        		var adpwd = $.cookie('adpwd');
        		if(adname != null && adpwd != null){
        			$("#accountname").val(adname);
        			$("#password").val(adpwd);
        			rrm = false;
        			document.getElementById("cbox").checked=true;
        		}else{
        			rrm = true;
        		}
        	}

        	// 重置
        	$('#forgetpass').click(function(e) {
        		$(":input").each(function() {
        			$('#'+this.name).val("");
        		});
        	});

        	// 点击登录
        	$('#but_login').click(function(e) {
        		submit();
        		saveCookies();
        	});
        	//存cookies，根据是否选中记住密码  存储cookies只能为一周
        	function saveCookies(){
        		if(document.getElementById("cbox").checked==true){
        			var md5pwd = "";
        			if($.cookie('adpwd') != $("#password").val()){
        				rrm = true;
        				md5pwd = $.md5($("#password").val());
        			}else{
        				rrm = false;
        			}
        			var p = md5pwd;
        			var n = $("#accountname").val();

        			if($("#accountname").val() == $.cookie('adname') && $("#password").val() == $.cookie('adpwd')){
        				rrm = false;
        			}else{
        				$.cookie('adname', '', { expires: -1 });
        				$.cookie('adpwd', '', { expires: -1 });
        				$.cookie("adname", n, { expires: 7 });
        				$.cookie("adpwd", p, { expires: 7 });
        			}
        		}else{
        			if($.cookie('adpwd') != "" || $.cookie('adname').length != ""){
        				$.cookie('adname', '', { expires: -1 });
        				$.cookie('adpwd', '', { expires: -1 });
        			}
        		}
        	}

        	//回车登录
        	$(document).keydown(function(e){
        		if(e.keyCode == 13) {
        			saveCookies();
        			submit();
        		}
        	});
        	$(function(){
        		optErrMsg();
        	});
        	$("#errMsgContiner").hide();
        	$('#login').show().animate({
        		opacity : 1
        	}, 2000);
        	$('.logo').show().animate({
        		opacity : 1,
        		top : '32%'
        	}, 800, function() {
        		$('.logo').show().delay(1200).animate({
        			opacity : 1,
        			top : '1%'
        		}, 300, function() {
        			$('.formLogin').animate({
        				opacity : 1,
        				left : '0'
        			}, 300);
        			$('.userbox').animate({
        				opacity : 0
        			}, 200).hide();
        		});
        	});

        	$('#password').change(function() {
        		rrm = true;
        	});

        	$('.userload').click(function(e) {
        		$('.formLogin').animate({
        			opacity : 1,
        			left : '0'
        		}, 300);
        		$('.userbox').animate({
        			opacity : 0
        		}, 200, function() {
        			$('.userbox').hide();
        		});
        	});
        	$('#randCodeImage').click(function(){
        		reloadRandCodeImage();
        	});
        	//点击消息关闭提示
        	$('#alertMessage').click(function() {
        		hideTop();
        	});

        	function optErrMsg(){
            		$("#showErrMsg").html('');
            		$("#errMsgContiner").hide();
            	};

            	//验证码输入框按下回车
            	function randCodeKeyDown(){
            		var lKeyCode = (navigator.appname=="Netscape")?event.which:window.event.keyCode; //event.keyCode按的建的代码，13表示回车
            		if(lKeyCode == 13){
            			checkUser();
            		}else{
            			return false;
            		}
            	}
            	//表单提交
            	function submit(){
            		var submit = true;
            		$("input[nullmsg]").each(function() {
            			if ($("#" + this.name).val() == "") {
            				showError($("#" + this.name).attr("nullmsg"), 500);
            				submit = false;
            				return false;
            			}
            		});
            		if (submit) {
            			hideTop();
            			if(checkUser()){
            				Login();
            			};
            		}
            	}
            	//验证用户信息
            	function checkUser(){
            		if(!validForm()){
            			return false;
            		}
            		return true;
            	}
            	//表单验证
            	function validForm(){
            		if($.trim($("#accountname").val()).length==0){
            			alert("请输入账户名");
            			return false;
            		}

            		if($.trim($("#password").val()).length==0){
            			alert("请输入密码");
            			return false;
            		}

            		if($.trim($("#randCode").val()).length==0){
            			alert("请输入验证码");
            			return false;
            		}
            		return true;
            	}
            	/**
            	 * 刷新验证码
            	 */
            	function reloadRandCodeImage() {
            		var date = new Date();
            		var img = document.getElementById("randCodeImage");
            		img.src='randCodeImage?a=' + date.getTime();
            	}

            	//登录处理函数
            	function Login(orgId) {
            		var formData = new Object();
            		var data=$(":input").each(function() {
            			formData[this.name] =$("#"+this.name ).val();
            			if(this.name == "password" && rrm){
            				formData[this.name] = $.md5($("#"+this.name ).val());
            			}
            		});
            		$.ajax({
            			async : false,
            			cache : false,
            			type : 'POST',
            			url : 'rest/rbac/loginController/login?Esale=true',// 请求的路径
            			data : formData,
            			dataType:"json",
            			error : function() {// 请求失败处理函数
            				alert("您输入的账户名或密码不正确，请重新输入");
            			},
            			success : function(data) {
            				if (data.msg=="randCode") {//返回值为success
            					alert("验证码输入错误！请重新输入");
            				}
            				if (data.msg=="success") {//返回值为success
            					loginSuccess();
            				}
            				if(data.msg=="pswFail"){
            					alert("您输入的密码不正确，请重新输入");
            				}
            				if(data.msg=="fail"){
            					alert("您输入的账户名或密码不正确，请重新输入");
            				}
            				if (data.have=="have") {
            					alert("您已经在此浏览器上进行了登录，请勿重复登录");
            				}
            			}
            		});
            	}

            	//显示错误提示
            	function showError(str) {
            		$('#alertMessage').addClass('error').html(str).stop(true, true).show().animate({
            			opacity : 1,
            			right : '0'
            		}, 500);

            	}

            	function showSuccess(str) {
            		$('#alertMessage').removeClass('error').html(str).stop(true, true).show().animate({
            			opacity : 1,
            			right : '0'
            		}, 500);
            	}

            	function hideTop() {
            		$('#alertMessage').animate({
            			opacity : 0,
            			right : '-20'
            		}, 500, function() {
            			$(this).hide();
            		});
            	}
            	//加载信息
            	function loading(name, overlay) {
            		$('body').append('<div id="overlay"></div><div id="preloader">' + name + '..</div>');
            		if (overlay == 1) {
            			$('#overlay').css('opacity', 0.1).fadeIn(function() {
            				$('#preloader').fadeIn();
            			});
            			return false;
            		}
            		$('#preloader').fadeIn();
            	}

            	function unloading() {
            		$('#preloader').fadeOut('fast', function() {
            			$('#overlay').fadeOut();
            		});
            	}

            	//登陆成功以后跳转页面
            	function loginSuccess(){
            		//登陆成功以后，根据userid，获取该用户可以访问的资源
            		window.location.href="rest/esale/EsaleHomeController?systemUrl=${systemUrl}";
            	};

            	//联系我们点击事件
            	$("#links").click(function(e){
            		$("#linkDialog").css("display", "block");
            	});
            	$("#cl").click(function(e){
            		$("#linkDialog").css("display", "none");
            	});
