
            function fixHeight() {
                var headerHeight = $("#switcher").height();
                $("#iframe").attr("height", $(window).height()-54+ "px");
            }
            $(window).resize(function () {
                fixHeight();
            }).resize();

            $('.icon-monitor').addClass('active');

            $(".icon-mobile-3").click(function () {
                $("#by").css("overflow-y", "auto");
                $('#iframe-wrap').removeClass().addClass('mobile-width-3');
                $('.icon-tablet,.icon-mobile-1,.icon-monitor,.icon-mobile-2,.icon-mobile-3').removeClass('active');
                $(this).addClass('active');
                return false;
            });

            $(".icon-mobile-2").click(function () {
                $("#by").css("overflow-y", "auto");
                $('#iframe-wrap').removeClass().addClass('mobile-width-2');
                $('.icon-tablet,.icon-mobile-1,.icon-monitor,.icon-mobile-2,.icon-mobile-3').removeClass('active');
                $(this).addClass('active');
                return false;
            });

            $(".icon-mobile-1").click(function () {
                $("#by").css("overflow-y", "auto");
                $('#iframe-wrap').removeClass().addClass('mobile-width');
                $('.icon-tablet,.icon-mobile,.icon-monitor,.icon-mobile-2,.icon-mobile-3').removeClass('active');
                $(this).addClass('active');
                return false;
            });

            $(".icon-tablet").click(function () {
                $("#by").css("overflow-y", "auto");
                $('#iframe-wrap').removeClass().addClass('tablet-width');
                $('.icon-tablet,.icon-mobile-1,.icon-monitor,.icon-mobile-2,.icon-mobile-3').removeClass('active');
                $(this).addClass('active');
                return false;
            });

            $(".icon-monitor").click(function () {
                $("#by").css("overflow-y", "hidden");
                $('#iframe-wrap').removeClass().addClass('full-width');
                $('.icon-tablet,.icon-mobile-1,.icon-monitor,.icon-mobile-2,.icon-mobile-3').removeClass('active');
                $(this).addClass('active');
                return false;
            });
  function Responsive($a) {
            if ($a == true) $("#Device").css("opacity", "100");
            if ($a == false) $("#Device").css("opacity", "0");
            $('#iframe-wrap').removeClass().addClass('full-width');
            $('.icon-tablet,.icon-mobile-1,.icon-monitor,.icon-mobile-2,.icon-mobile-3').removeClass('active');
            $(this).addClass('active');
            return false;
        };

          $(function(){
                    $(".li1").hover(function(){
                        $(".shoucang").show();/*��ʾ�ղصĲ˵�����*/
                        $(".customer,.sell").hide();/*���������Ĳ˵�����*/
                    },function(){
                        $(".shoucang").hide();/*����뿪���ղصĲ˵���������*/
                    });
                    $(".li2").hover(function(){
                        $(".sell").show();
                        $(".customer,.shoucang").hide();
                    },function(){
                        $(".sell").hide();
                    });
                    $(".li3").hover(function(){
                        $(".customer").show();
                        $(".shoucang,.sell").hide();
                    },function(){
                        $(".customer").hide();
                    });
                    $("#top div").hover(function(){/*��������divλ���ϣ���ʾ�����˵�*/
                        $(this).show();
                    },function(){/*����뿪��divλ���ϣ����������˵�*/
                        $(this).hide();
                    });
                    $(".sell p,.shoucang p,.customer p").bind("mouseover",function(){
                        $(this).addClass("bg");
                    });
                    $(".sell p,.shoucang p,.customer p").bind("mouseout",function(){
                        $(this).removeClass("bg");
                    });
                });