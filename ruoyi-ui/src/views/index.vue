<template>
    <div>
        <div class="page-wrapper">

            <!-- Preloader -->
            <!-- <div class="preloader"></div> -->
        
            <!-- Main Header-->
            <header class="main-header">
                <!-- Header Top -->
                <div class="header-top">
                    <div class="auto-container1 clearfix">
                        <!--Top Left-->
                        <div class="top-left pull-left">
                            <ul class="links-nav clearfix">
                                <li><a href="#">欢迎进入!</a></li>
                            </ul>
                        </div>
        
                        <!--Top Right-->
                        <div class="top-right pull-right">
                            <ul v-if="!login" class="links-nav clearfix">
        <!--                        <li><a href="#">About us</a></li>-->
                                <li><a @click="$router.push('/register')">注册</a></li>
                                <li><a @click="$router.push('/login')" >登录</a></li>
                            </ul>
                            <ul v-else class="links-nav clearfix">
                                <!--                        <li><a href="#">About us</a></li>-->
                                <li><a>{{name}}</a></li>
                                <li><a @click="logout" >退出登录</a></li>
                            </ul>
                        </div>
                    </div>
                </div><!-- Header Top End -->
        
        <!--        Header-Upper-->
                <div class="header-upper">
                    <div class="auto-container">
                        <div class="clearfix">
        
                            <div class="pull-left logo-outer col-md-12">
                                <div class="logo col-md-4" ><a href="#"><img src="images/ok.jpg" alt="Transpo" title="Transpo"/></a></div>
                                 <div class="name col-md-8">大气污染数据聚类分析系统</div>
                            </div>
        
        
        
                        </div>
                    </div>
                </div>
        
                <!--Header-Lower-->
                <div class="header-lower">
                    <!--Background Layer-->
                    <div class="bg-layer"></div>
        
        
                    <div class="auto-container2">
                        <div class="nav-outer clearfix">
                            <!-- Main Menu -->
                            <nav class="main-menu">
                                <div class="navbar-header">
                                    <!-- Toggle Button -->
                                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                                        <span class="icon-bar"></span>
                                        <span class="icon-bar"></span>
                                        <span class="icon-bar"></span>
                                    </button>
                                </div>
        
                                <div class="navbar-collapse collapse clearfix">
                                    <ul class="navigation clearfix">
                                        <li class="current dropdown"><a href="#">首页</a>
                                            <!-- <ul>
                                                 <li><a href="#">Homepage One</a></li>
                                                 <li><a href="#">Homepage Two</a></li>
                                             </ul> -->
                                        </li>
                                        <li class="dropdown"><a @click="cluster">密度聚类算法</a>
                                            <ul>
                                                <li><a @click="moveToKMeans">k-Means</a></li>
                                                <li><a @click="moveToDBSCAN">DBSCAN</a></li>
                                                <li><a @click="moveToDP">DP</a></li>
                                                <li><a @click="moveCLUB">CLUB</a></li>
                                                <li><a @click="moveToEPC">EPC</a></li>
        <!--                                        <li><a href="#">Storage &amp; Logistic</a></li>-->
                                            </ul>
                                        </li>
                                        <li class="dropdown"><a href="two">图聚类算法</a>
                                            <ul>
                                                <li><a @click="moveToSSC">SSC</a></li>
                                                <!-- <li><a @click="$router.push('/index_ssc')">SSC</a></li> -->
                                                <li><a @click="moveToLRR">LRR</a></li>
                                                <li><a @click="moveToRcut">Rcut</a></li>
                                                <li><a @click="moveToNcut">Ncut</a></li>
                                                <li><a @click="moveToSP">SP</a></li>
        
                                            </ul>
                                        </li>
                                        <!-- <li class="dropdown"><a href="#">News</a>
                                            <ul>
                                                <li><a href="blog.html">Our News</a></li>
                                                <li><a href="blog-single.html">News Details</a></li>
                                            </ul>
                                        </li>
                                        <li><a href="contact.html">Contact Us</a></li> -->
                                        <li><a href="#three">关于我们</a></li>
                                    </ul>
                                </div>
                            </nav><!-- Main Menu End-->
        
        
        
        
                        </div>
                    </div>
                </div>
        
        <!--    </header>-->
        <!--</div>-->
        
                <!--Sticky Header-->
                <div class="sticky-header">
                    <div class="auto-container clearfix">
                        <!--Logo-->
                        <div class="logo pull-left">
                            <img src="@/assets/images/logo12(2).png"/>
                        </div>
        
                        <!--Right Col-->
                        <div class="right-col pull-right">
                            <!-- Main Menu -->
                            <nav class="main-menu">
                                <div class="navbar-header">
                                    <!-- Toggle Button -->
                                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                                        <span class="icon-bar"></span>
                                        <span class="icon-bar"></span>
                                        <span class="icon-bar"></span>
                                    </button>
                                </div>
        
                                <div class="navbar-collapse collapse clearfix">
                                    <ul class="navigation clearfix">
                                        <li class="current dropdown"><a href="#">首页</a>
        <!--                                    <ul>-->
        <!--                                        <li><a href="#">首页</a></li>-->
        <!--&lt;!&ndash;                                        <li><a href="#">Homepage Two</a></li>&ndash;&gt;-->
        <!--                                    </ul>-->
                                        </li>

                                        <li class="dropdown"><a href="#one">密度聚类算法</a>
                                        </li>
                                        <li class="dropdown"><a href="#two">图聚类算法</a>
        <!--                                    <ul>-->
        <!--                                        <li><a href="blog.html">Our News</a></li>-->
        <!--                                        <li><a href="blog-single.html">News Details</a></li>-->
        <!--                                    </ul>-->
                                        </li>
                                        <li><a href="#three">关于我们</a></li>
                                    </ul>
                                </div>
                            </nav><!-- Main Menu End-->
                        </div>
        
                    </div>
                </div><!--End Sticky Header-->
        
            </header>
            <!--End Main Header -->
        
        
            <!--Main Slider-->
            <section class="main-slider negative-margin">

                <!--        <div class="tp-banner-container">-->
                <div style="position:relative;width:100%;height:auto;margin-top:0px;margin-bottom:0px" class="forcefullwidth_wrapper_tp_banner"><div class="auth-container" style="left: 0px; position: absolute; width: 1897px; overflow: visible;">
                    <div class="tp-banner revslider-initialised tp-simpleresponsive hovered" id="revslider-683" style="max-height: 880px; background-color: rgba(0, 0, 0, 0); background-image: none; height: 880px;">
                        <ul class="tp-revslider-mainul" style="display: block; overflow: hidden; width: 100%; height: 100%; max-height: none;">
    
                            <li data-transition="fade" data-slotamount="1" data-masterspeed="1000" data-thumb="images/main-slider/4.jpg" data-saveperformance="off" data-title="Awesome Title Here" class="tp-revslider-slidesli active-revslide" style="width: 100%; height: 100%; overflow: hidden; z-index: 18; visibility: hidden; opacity: 0;">
                                <div class="slotholder" style="width:100%;height:100%;" data-duration="undefined" data-zoomstart="undefined" data-zoomend="undefined" data-rotationstart="undefined" data-rotationend="undefined" data-ease="undefined" data-bgpositionend="undefined" data-bgposition="center top" data-kenburns="undefined" data-easeme="undefined" data-bgfit="cover" data-bgfitend="undefined" data-owidth="undefined" data-oheight="undefined"><div class="tp-bgimg defaultimg" data-lazyload="undefined" data-bgfit="cover" data-bgposition="center top" data-bgrepeat="no-repeat" data-lazydone="undefined" src="images/main-slider/4.jpg" data-src="images/main-slider/4.jpg" style="background-color: rgba(0, 0, 0, 0); background-repeat: no-repeat; background-image: url(&quot;images/main-slider/4.jpg&quot;); background-size: cover; background-position: center top; width: 100%; height: 100%; opacity: 1; visibility: inherit;"></div><div class="tp-dottedoverlay yes"></div></div>
    
                                <div class="tp-caption sfl sfb tp-resizeme start" data-x="left" data-hoffset="15" data-y="center" data-voffset="-70" data-speed="1500" data-start="0" data-easing="easeOutExpo" data-splitin="none" data-splitout="none" data-elementdelay="0.01" data-endelementdelay="0.3" data-endspeed="1200" data-endeasing="Power4.easeIn" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 25px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 14px; left: 363.5px; top: 354px; visibility: visible; opacity: 0; transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.00166, -50, 0, 0, 1);">
                                    <div class="italic-text" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 32px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 18px;">Welcome </div>
                                </div>
    
                                <div class="tp-caption sfl sfb tp-resizeme start" data-x="left" data-hoffset="15" data-y="center" data-voffset="10" data-speed="1500" data-start="500" data-easing="easeOutExpo" data-splitin="none" data-splitout="none" data-elementdelay="0.01" data-endelementdelay="0.3" data-endspeed="1200" data-endeasing="Power4.easeIn" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 25px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 14px; left: 363.5px; top: 393px; visibility: visible; opacity: 0; transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.00166, -50, 0, 0, 1);">
                                    <h2 style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 57px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 48px;">We Provide <br style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 57px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 48px;">Data Analysis</h2>
                                </div>
    
                                <div class="tp-caption sfr sfb tp-resizeme start" data-x="left" data-hoffset="15" data-y="center" data-voffset="110" data-speed="1500" data-start="1000" data-easing="easeOutExpo" data-splitin="none" data-splitout="none" data-elementdelay="0.01" data-endelementdelay="0.3" data-endspeed="1200" data-endeasing="Power4.easeIn" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 25px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 14px; left: 363.5px; top: 516.5px; visibility: visible; opacity: 0; transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.00166, 50, 0, 0, 1);"><a href="#" class="theme-btn btn-style-one" style="transition: all 0.2s ease-out 0s; min-height: 0px; min-width: 0px; line-height: 26px; border-width: 2px; margin: 0px 0px 20px; padding: 8px 25px 9px; letter-spacing: 0px; font-size: 16px;">了解更多 <span class="fa fa-caret-right" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 26px; border-width: 0px; margin: 0px; padding: 0px 0px 0px 7px; letter-spacing: 0px; font-size: 16px;"></span></a></div>
    
                                <div class="tp-caption sfr sfb tp-resizeme start" data-x="left" data-hoffset="290" data-y="center" data-voffset="110" data-speed="1500" data-start="1500" data-easing="easeOutExpo" data-splitin="none" data-splitout="none" data-elementdelay="0.01" data-endelementdelay="0.3" data-endspeed="1200" data-endeasing="Power4.easeIn" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 25px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 14px; left: 638.5px; top: 526.5px; visibility: visible; opacity: 0; transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.00166, 50, 0, 0, 1);">
                                    
                                </div>
    
    
                            </li>
    
                            <li data-transition="fade" data-slotamount="1" data-masterspeed="1000" data-thumb="images/main-slider/5.jpg" data-saveperformance="off" data-title="Awesome Title Here" class="tp-revslider-slidesli active-revslide current-sr-slide-visible" style="width: 100%; height: 100%; overflow: hidden; visibility: inherit; opacity: 1; z-index: 20;">
                                <div class="slotholder" style="width:100%;height:100%;" data-duration="undefined" data-zoomstart="undefined" data-zoomend="undefined" data-rotationstart="undefined" data-rotationend="undefined" data-ease="undefined" data-bgpositionend="undefined" data-bgposition="center top" data-kenburns="undefined" data-easeme="undefined" data-bgfit="cover" data-bgfitend="undefined" data-owidth="undefined" data-oheight="undefined"><div class="tp-bgimg defaultimg" data-lazyload="undefined" data-bgfit="cover" data-bgposition="center top" data-bgrepeat="no-repeat" data-lazydone="undefined" src="images/main-slider/5.jpg" data-src="images/main-slider/5.jpg" style="background-color: rgba(0, 0, 0, 0); background-repeat: no-repeat; background-image: url(&quot;images/main-slider/5.jpg&quot;); background-size: cover; background-position: center top; width: 100%; height: 100%; opacity: 1; visibility: inherit;"></div><div class="tp-dottedoverlay yes"></div></div>
    
                                <div class="tp-caption sfl sfb tp-resizeme start" data-x="left" data-hoffset="15" data-y="center" data-voffset="-70" data-speed="1500" data-start="0" data-easing="easeOutExpo" data-splitin="none" data-splitout="none" data-elementdelay="0.01" data-endelementdelay="0.3" data-endspeed="1200" data-endeasing="Power4.easeIn" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 25px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 14px; left: 363.5px; top: 354px; visibility: visible; opacity: 1; transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.0025, 0, 0, 0, 1);">
                                    <div class="italic-text" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 32px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 18px;">Welcome</div>
                                </div>
    
                                <div class="tp-caption sfl sfb tp-resizeme start" data-x="left" data-hoffset="15" data-y="center" data-voffset="10" data-speed="1500" data-start="500" data-easing="easeOutExpo" data-splitin="none" data-splitout="none" data-elementdelay="0.01" data-endelementdelay="0.3" data-endspeed="1200" data-endeasing="Power4.easeIn" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 25px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 14px; left: 363.5px; top: 393px; visibility: visible; opacity: 1; transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.0025, 0, 0, 0, 1);">
                                    <h2 style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 57px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 48px;">We Provide <br style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 57px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 48px;">Data Analysis</h2>
                                </div>
    
                                <div class="tp-caption sfr sfb tp-resizeme start" data-x="left" data-hoffset="15" data-y="center" data-voffset="110" data-speed="1500" data-start="1000" data-easing="easeOutExpo" data-splitin="none" data-splitout="none" data-elementdelay="0.01" data-endelementdelay="0.3" data-endspeed="1200" data-endeasing="Power4.easeIn" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 25px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 14px; left: 363.5px; top: 516.5px; visibility: visible; opacity: 1; transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.0025, 0, 0, 0, 1);"><a a @click="$router.push('/login')" class="theme-btn btn-style-one" style="transition: all 0.2s ease-out 0s; min-height: 0px; min-width: 0px; line-height: 26px; border-width: 2px; margin: 0px 0px 20px; padding: 8px 25px 9px; letter-spacing: 0px; font-size: 16px;">了解更多 <span class="fa fa-caret-right" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 26px; border-width: 0px; margin: 0px; padding: 0px 0px 0px 7px; letter-spacing: 0px; font-size: 16px;"></span></a></div>
    
                                <div class="tp-caption sfr sfb tp-resizeme start" data-x="left" data-hoffset="290" data-y="center" data-voffset="110" data-speed="1500" data-start="1500" data-easing="easeOutExpo" data-splitin="none" data-splitout="none" data-elementdelay="0.01" data-endelementdelay="0.3" data-endspeed="1200" data-endeasing="Power4.easeIn" style="transition: all 0s ease 0s; min-height: 0px; min-width: 0px; line-height: 25px; border-width: 0px; margin: 0px; padding: 0px; letter-spacing: 0px; font-size: 14px; left: 638.5px; top: 526.5px; visibility: visible; opacity: 1; transform: matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, -0.0025, 0, 0, 0, 1);">
                                    <a @click="$router.push('/login')" class="theme-btn btn-style-two" style="transition: all 0.2s ease-out 0s; min-height: 0px; min-width: 0px; line-height: 26px; border-width: 2px; margin: 0px; padding: 8px 25px 9px; letter-spacing: 0px; font-size: 16px;">开始分析</a>
                                    </div>
    
    
                            </li>
    
                        </ul>
    
                        <div class="tp-bannertimer" style="width: 30.2%; visibility: visible; transform: translate3d(0px, 0px, 0px);"></div>
                    <div class="tp-loader spinner4" style="display: none;"><div class="dot1"></div><div class="dot2"></div><div class="bounce1"></div><div class="bounce2"></div><div class="bounce3"></div></div></div>
                <div class="tp-bullets simplebullets preview3" style="bottom: 40px; left: 50%; margin-left: -33px;"><div class="bullet first"></div><div class="bullet last selected"></div><div class="tpclear"></div></div><div style="position: absolute; top: 440px; margin-top: -25px; left: 0px;" class="tp-leftarrow tparrows default preview3"><div class="tp-arr-allwrapper"><div class="tp-arr-iwrapper"><div class="tp-arr-imgholder" style="visibility: inherit; opacity: 1; background-image: url(&quot;images/main-slider/4.jpg&quot;);"></div><div class="tp-arr-imgholder2"></div><div class="tp-arr-titleholder">Awesome Title Here</div><div class="tp-arr-subtitleholder"></div></div></div></div><div style="position: absolute; top: 440px; margin-top: -25px; right: 0px;" class="tp-rightarrow tparrows default preview3"><div class="tp-arr-allwrapper"><div class="tp-arr-iwrapper"><div class="tp-arr-imgholder" style="visibility: inherit; opacity: 1; background-image: url(&quot;images/main-slider/4.jpg&quot;);"></div><div class="tp-arr-imgholder2"></div><div class="tp-arr-titleholder">Awesome Title Here</div><div class="tp-arr-subtitleholder"></div></div></div></div></div><div class="tp-fullwidth-forcer" style="width:100%;height:880px"></div></div>
            </section>
        
        
            <!--Featured Services-->
            <section class="featured-services">
                <div class="auto-container">
                    <div class="sec-title text-center">
                        <h2>Your trusted logistic partner in worldwide </h2>
                        <div class="text">Lorem ipsum dolor sit amet, <strong>usu no alienum adolescens</strong> an principes liberavisse eam, nam wisi malis recusabo at.</div>
                    </div>
                    <div class="text-center"><a href="#" class="theme-btn btn-style-four">带您了解污染物 <span class="fa fa-caret-right"></span></a></div>
                 <div class="col-md-12">
                    <div class="col-md-6 " >
                        <table class="mt">
                            <caption style="text-align: center">空气污染指数(AQI)标准对应表</caption>
                            <tr>
                                <th>空气质量指数</th>
                                <th>空气质量状况</th>
                                <th>对健康影响情况</th>
                                <th>建议采取的措施</th>
                            </tr>
                            <tr>
                                <td>0-50</td>
                                <td>优</td>
                                <td>空气质量令人满意，基本无空气污染，对健康没有危害</td>
                                <td>各类人群可多参加户外活动，多呼吸一下清新的空气</td>
        
                            </tr>
                            <tr>
                                <td>51-100</td>
                                <td>良</td>
                                <td>可能对少数异常敏感人群健康有较弱影响</td>
                                <td>正常进行室外活动</td>
        
                            </tr>
                            <tr>
                                <td>101-150</td>
                                <td>轻污染</td>
                                <td>易感人群症状加剧,健康人群出现刺激症状</td>
                                <td>特殊群体减少体力消耗大的户外活动</td>
        
                            </tr>
                            <tr>
                                <td>151-200</td>
                                <td>中污染</td>
                                <td>进一步加剧易感人群症状和对健康人群的影响</td>
                                <td>一般人群尽量减少户外活动</td>
        
                            </tr>
                            <tr>
                                <td>201-250</td>
                                <td>重污染</td>
                                <td>心脏病患者症状显著加剧,健康人群普遍出现症状</td>
                                <td>一般人群减少户外活动</td>
        
                            </tr>
                            <tr>
                                <td>>300</td>
                                <td>重污染</td>
                                <td>健康人群运动耐受力降低，有强烈症状</td>
                                <td>一般人群尽量不要滞留在室外</td>
        
                            </tr>
                        </table>
                    </div>
        
                     <div class="col-md-6 " >
                         <table class="mt">
                             <caption style="text-align: center">各污染物对⼈体伤害情况</caption>
                             <tr>
                                 <th>选取指标</th>
                                 <th>污染物危害</th>
                                 <td>单位</td>
        
                             </tr>
                             <tr>
                                 <td>PM2.5</td>
                                 <td>细颗粒物由于径小，富含大量的有毒、有害物质，且在大气中停留时间长、传播距离远，因此对人的身体健康和环境的质量影响很大</td>
                                 <td>ug/m³</td>
        
        
                             </tr>
                             <tr>
                                 <td>PM10</td>
                                 <td>可吸入颗粒物的直径比细颗粒物的直径大，它可以被人体吸入，会累积在呼吸系统中，引发许多疾病</td>
                                 <td>ug/m³</td>
        
        
                             </tr>
                             <tr>
                                 <td>SO2</td>
                                 <td>SO2是一种无色但具有强烈刺激性气味的气体。易溶于人体的血液和其他黏性液;低浓度下的SO2会造成植物产量下降;形成酸雨和酸雾</td>
                                 <td>ug/m³</td>
        
        
                             </tr>
                             <tr>
                                 <td>CO</td>
                                 <td>CO是一种对血液与神经系统毒性很强的污染物，长时间接触低浓度的一氧化碳对人体心血管系统、神经系统乃至对后代均有一定影响</td>
                                 <td>mg/m³</td>
        
                             </tr>
                             <tr>
                                 <td>NO2</td>
                                 <td>会损害人体呼吸道;对水体、土壤和大气均可造成污染</td>
                                 <td>ug/m³</td>
        
                             </tr>
                             <tr>
                                 <td>03</td>
                                 <td>刺激人体呼吸道，引发支气管炎和肺气肿，造成神经中毒，头晕头痛，破坏人体皮肤中的维生素E;身体健康的人们对臭氧也更敏感些;抑制多种作物以及树种的生长</td>
                                 <td>ug/m³</td>
        
                             </tr>
                         </table>
                     </div>
                </div>
                </div>
            </section>
        
            <section class="featured-services">
                <div class="auto-container">
                    <div class="sec-title text-center">
                     <div class="row clearfix">
                
                         <div class="text-center"><a href="#" class="theme-btn btn-style-one" id="one">密度聚类算法<span class="fa fa-caret-right"></span></a></div>
                        <div class="row">
                            <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                <h3><a href="#">k-Means聚类方法 <br>Solution &amp; Distribution</a></h3>
        <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                            </div>
                            <div class="lower-content col-md-8" >
        <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                    <div class="text">k-Means算法是一种无监督学习，同时也是基于划分的聚类算法，是目前最常用的聚类方法之一。其中心思想是：算法预先指定初始聚类数目k以及k个初始聚类中心，根据数据对象与聚类中心之间的相似度，不断更新聚类中心的位置，直到簇中心点不再改变或者迭代次数达到设定限值。一般用欧式距离作为衡量数据对象间相似度的指标，相似度与数据对象间的距离成反比，相似度越大，距离越小。</div>
                            </div>
        
                        </div>
                         <div class="row">
                             <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                 <h3><a href="#">密度峰值聚类算法 <br>DP</a></h3>
                                 <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                             </div>
                             <div class="lower-content col-md-8" >
                                 <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                 <div class="text">主要思想是根据距离和密度峰值对数据进行划分。与k-Means等算法不同的是，DP算法不需要迭代，聚类中心可以一次性找到。该算法实现要满足两个条件：
                                    条件A：簇中心，也就是密度峰值点的局部密度大于围绕它的邻居的局部密度；
                                    条件B：不同簇中心之间的距离相对较远。</div>
                             </div>
        
                         </div>
                         <div class="row">
                            <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                <h3><a href="#"> <br>EPC</a></h3>
                                <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                            </div>
                            <div class="lower-content col-md-8" >
                                <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                <div class="text">EPC聚类算法的主要思想是根据相似性函数对数据进行划分，其值取值范围在[0,1]，值越大相似度也随之越高。通常，数据无缺失时达不到零值。EPC算法有一个阈值输入，其代表数据间的相似度。阈值的选取是EPC算法中非常重要的一步，阈值设置的越高，簇内数据相似度就越高。</div>
                            </div>
        
                        </div>
                        <div class="row">
                            <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                <h3><a href="#"> <br>CLUB聚类算法</a></h3>
                                <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                            </div>
                            <div class="lower-content col-md-8" >
                                <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                <div class="text">CLUB算法中初始簇的形成采用的是互k最近邻方法，互k最近邻方法的思想是，如果某个数据点的k个最近邻大多数都在某一个簇中，则该数据点也被划分到这个簇中并且与该簇样本具有相似的特征。算法的第二步输入是第一步得到的初始簇结构，利用k最近邻法对聚类进行高密度主干的识别。最后，算法检测出簇内的异常点。因而，CLUB算法能够发现任意形状、任意密度的簇。</div>
                            </div>
        
                        </div>
                        <div class="row">
                            <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                <h3><a href="#"> <br>DBSCAN</a></h3>
                                <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                            </div>
                            <div class="lower-content col-md-8" >
                                <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                <div class="text"> DBSCAN是一种基于密度的空间聚类算法，是一种无监督的ML聚类算法。该算法将具有足够密度的区域划分为簇，并在具有噪声的空间数据库中发现任意形状的簇，它将簇定义为密度相连的点的最大集合。</div>
                            </div>
        
                        </div>
        
                    </div>
         
                    </div>
                </div>
        </section>
        
            <section class="featured-services">
                <div class="auto-container">
                    <div class="sec-title text-center">
                        <div class="row clearfix">
                      
                            <div class="text-center"><a href="#" class="theme-btn btn-style-one" id="two">图聚类算法 <span class="fa fa-caret-right"></span></a></div>
                            <div class="row">
                                <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                    <h3><a href="#">谱聚类算法 <br>Spectral clustering</a></h3>
                                    <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                                </div>
                                <div class="lower-content col-md-8" >
                                    <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                    <div class="text">谱聚类是一种基于图论的聚类方法——将带权无向图划分为两个或两个以上的最优子图，使子图内部尽量相似，而子图间距离尽量距离较远，以达到常见的聚类的目的。谱聚类能够识别任意形状的样本空间且收敛于全局最优解，其基本思想是利用样本数据的相似矩阵(拉普拉斯矩阵)进行特征分解后得到的特征向量进行聚类。</div>
                                </div>
        
                            </div>
                            <div class="row">
                                <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                    <h3><a href="#">稀疏表示子空间聚类 <br>Low-Rank Representation,LRR</a></h3>
                                    <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                                </div>
                                <div class="lower-content col-md-8" >
                                    <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                    <div class="text">LRR的方法来对原始的图像进行分解，得到低秩部分（全局结构）和显著部分（局部结构），然后低秩部分使用加权平均的策略进行融合，显著部分使用求和的方式进行融合，最终重建出融合图像。
                                        算法可以分为三个步骤：①图片分解，分解为低秩部分和显著部分；②使用两种不同的融合策略对低秩部分和显著部分图像进行融合；③重建图像。</div>
                                </div>
        
                            </div>
                            <div class="row">
                                <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                    <h3><a href="#">低秩表示子空间聚类 <br>Sparse Subspace Clustering,SSC</a></h3>
                                    <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                                </div>
                                <div class="lower-content col-md-8" >
                                    <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                    <div class="text">稀疏子空间聚类算法，该算法用于使用稀疏表示技术对从子空间的并集得出的点进行聚类。 SSC算法分两个步骤解决子空间聚类问题：第一步，对于每个数据点，我们找到属于同一子空间的其他一些点。为此，我们解决了一个全局稀疏优化程序，该程序的解决方案将有关数据点成员资格的信息编码到每个点的基础子空间。在第二步中，我们在谱聚类框架中使用此信息来推断数据的聚类。</div>
                                </div>
        
                            </div>
                            <div class="row">
                                <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                    <h3><a href="#">Normalized cut算法 <br>Ncut</a></h3>
                                    <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                                </div>
                                <div class="lower-content col-md-8" >
                                    <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                    <div class="text">信息处理中的许多问题都涉及使用从数据派生的矩阵的特征向量进行某种形式的聚类点。 其中，N-cut和谱聚类在归一化切割准则的框架内。 N-cut 将聚类问题视为图划分问题，并提出了一种新颖的全局标准，即归一化割，用于对图进行分割。 归一化切割标准测量不同组之间的总差异以及组内的总相似度。 此外，谱聚类通过使用矩阵扰动理论中的工具将 K-means 与 N-cut 相结合，并在许多具有挑战性的聚类问题上取得了良好的实验结果。</div>
                                </div>
        
                            </div>
                            <div class="row">
                                <div class="inner-box wow fadeIn  col-md-4" data-wow-delay="0ms" data-wow-duration="1500ms">
                                    <h3><a href="#">Rcut <br></a></h3>
                                    <!--                        <figure class="image-box"><img src="../../images/homepage/resource/featured-image-1.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>-->
                                </div>
                                <div class="lower-content col-md-8" >
                                    <!--                            <h3><a href="services-single.html">Transport <br>Solution &amp; Distribution</a></h3>-->
                                    <div class="text"></div>
                                </div>
        
                            </div>
                        </div>
                       
                    </div>
                </div>
            </section>
        
            <section class="featured-services">
                <div class="auto-container">
                    <div class="sec-title text-center">
                    <div class="text-center"><a href="#" class="theme-btn btn-style-one" id="three">关于我们更多 <span class="fa fa-caret-right"></span></a></div>
                            <div class="row clearfix">
                                <!--Featured Service Column-->
                                <div class="featured-service-column col-md-4 col-sm-6 col-xs-12">
                                    <div class="inner-box wow fadeIn" data-wow-delay="0ms" data-wow-duration="1500ms">
                                        <figure class="image-box"><img src="../assets/images/resource/featured-image-1.jpg" alt=""/><a class="overlay" href="services-single.html"></a></figure>
                                        <div class="lower-content">
                                            <h3><a href="">聚类分析 <br>Graph Learning Clustering</a></h3>
                                            <div class="text">聚类分析是指通过不同的特征将研究对象分为不同的组，达到同一组类特征差别较小相似度较大而不同组间差别较大相似度较小的目的。选取合适的聚类算法对大气污染数据进行聚类分析，有助于找出大气污染的形成原因及应对方法。</div>
                                        </div>
                                    </div>
                                </div>
                                
                                <!--Featured Service Column-->
                                <div class="featured-service-column col-md-4 col-sm-6 col-xs-12">
                                    <div class="inner-box wow fadeIn" data-wow-delay="300ms" data-wow-duration="1500ms">
                                        <figure class="image-box"><img src="../assets/images/resource/featured-image-2.jpg" alt=""><a class="overlay" href="services-single.html"></a></figure>
                                        <div class="lower-content">
                                            <h3><a href="">聚类算法 <br>Clustering analysis</a></h3>
                                            <div class="text">从聚类分析角度出发，使用不同的聚类算法对数据进行聚类分析。对数据进行数据清洗和数据标准化，根据不同的算法选择数据集进行聚类</div>
                                        </div>
                                    </div>
                                </div>
                                
                                <!--Featured Service Column-->
                                <div class="featured-service-column col-md-4 col-sm-6 col-xs-12">
                                    <div class="inner-box wow fadeIn" data-wow-delay="600ms" data-wow-duration="1500ms">
                                        <figure class="image-box"><img src="../assets/images/resource/featured-image-3.jpg" alt=""/><a class="overlay" href="services-single.html"></a></figure>
                                        <div class="lower-content">
                                            <h3><a href="">大气污染 <br> Air Pollution</a></h3>
                                            <div class="text">污染物对空气质量有着显著的影响，在多种污染物中，PM2.5、PM10、SO2、NO2、CO和O3等已成为我国空气中的主要污染物。这些污染物的产生及排放对人类的生存环境产生了不良的影响。</div>
                                        </div>
                                    </div>
                                </div>
                                
                            </div>
                </div>
                </div>
            </section>
        
        
            <!--Fun Facts Section-->
            <section class="fun-fact-section" :style="'background-image:url('+ require('../assets/images/background/image-1.jpg')+ ')'">
                <div class="auto-container">
                    <div class="fact-counter">
                        <div class="clearfix">
        
                            <!--Column-->
                            <div class="col-md-3 col-sm-6 col-xs-12 column counter-column wow fadeIn" data-wow-duration="0ms">
                                <div class="inner">
                                    <div class="icon-box"><span class="flaticon-trophy-1"></span></div>
                                    <div class="count-outer"><span >中国</span></div>
                                    <h4 class="counter-title">Country</h4>
                                </div>
                            </div>
                            <!--Column-->
                            <div class="col-md-3 col-sm-6 col-xs-12 column counter-column wow fadeIn" data-wow-duration="0ms">
                                <div class="inner">
                                    <div class="icon-box"><span class="flaticon-placeholder"></span></div>
                                    <div class="count-outer"><span class="count-text" data-speed="1500" data-stop="596">0</span></div>
                                    <h4 class="counter-title">城市数量</h4>
                                </div>
                            </div>
        
                            <!--Column-->
                            <div class="col-md-3 col-sm-6 col-xs-12 column counter-column wow fadeIn" data-wow-duration="0ms">
                                <div class="inner">
                                    <div class="icon-box"><span class="flaticon-truck-2"></span></div>
                                    <div class="count-outer"><span class="count-text" data-speed="1500" data-stop="12">0</span></div>
                                    <h4 class="counter-title">分析算法</h4>
                                </div>
                            </div>
                            <!--Column-->
                            <div class="col-md-3 col-sm-6 col-xs-12 column counter-column wow fadeIn" data-wow-duration="0ms">
                                <div class="inner">
                                    <div class="icon-box"><span class="flaticon-avatar"></span></div>
                                    <div class="count-outer"><span class="count-text" data-speed="1500" data-stop="23W">0</span>k</div>
                                    <h4 class="counter-title">数据量</h4>
                                </div>
                            </div>
                           
        
        
                        </div>
                    </div>
                </div>
            </section>
        
        
   <!--Services Section-->
   <section class="services-section" :style="'background-image:url('+ require('../assets/images/background/image-2.jpg')+ ')'">
    <div class="auto-container">
        <div class="sec-title light text-center">
            <h1>我们的团队</h1>
            <div class="text">兰州交通大学 电子与信息工程学院</div>
        </div>

        <div class="row clearfix">
            <div class="default-service-block col-md-4 col-sm-6 col-xs-12">
                <div class="inner-box wow fadeInLeft" data-wow-delay="0ms" data-wow-duration="1500ms">
                    <div class="icon-box"><span class="flaticon-ship"></span></div>
                    <h3>简介</h3>
                    <div class="text">陈梅，兰州大学计算机应用技术博士，教授，博士生导师。荣获“兰州交通大学2017-2018年度科研先进个人”荣誉称号，并获得兰州交通大学“百名青年优秀人才培养计划（科研型）”资助。兼职国际学术会议DSS。在数据挖掘、复杂网络分析等方向开展深入研究。</div>
                </div>
            </div>

            <!--Default Service Block-->
            <div class="default-service-block col-md-4 col-sm-6 col-xs-12">
                <div class="inner-box wow fadeInDown" data-wow-delay="0ms" data-wow-duration="1500ms">
                    <div class="icon-box"><span class="flaticon-package"></span></div>
                    <h3>研究方向 </h3>
                    <div class="text">主要从事数据挖掘，机器学习等相关研究，包括但不限于：
                        1. 复杂网络分析<br>
                        2. 社团检测<br>
                        3. 密度聚类<br>
                        4. 图学习聚类<br>
                        5. 时间序列聚类<br>
                        6.复杂数据聚类<br>
                    </div>
                </div>
            </div>

            <!--Default Service Block-->
            <div class="default-service-block col-md-4 col-sm-6 col-xs-12">
                <div class="inner-box wow fadeInRight" data-wow-delay="0ms" data-wow-duration="1500ms">
                    <div class="icon-box"><span class="flaticon-passenger-train-front-view"></span></div>
                    <h3>招生信息</h3>
                    <div class="text">欢迎满足下列条件之一的同学报考：

                        （1）具有较好的数学及计算机基础者；
                        
                        （2）具有较好的英语水平者（阅读、写作及交流能力；<br>
                        
                        （3）对数据挖掘算法设计与应用有浓厚兴趣者；<br>
                        
                        （4）对系统开发有基础及浓厚兴趣者；</div>
                </div>
            </div>

            <!--Default Service Block-->
            <div class="default-service-block col-md-4 col-sm-6 col-xs-12">
                <div class="inner-box wow fadeInLeft" data-wow-delay="0ms" data-wow-duration="1500ms">
                    <div class="icon-box"><span class="flaticon-storage"></span></div>
                    <h3>科研团队 </h3>
                    <div class="text">
                        在研究团队方面，课题组已经形成了包括教授、副教授、讲师、博士生、硕士生、本科生等20多人的研究开发队伍，成员具有计算机科学与技术、数据科学与大数据技术、物联网、以及网络安全等技术背景，具有良好的科研基础、丰富的科研项目经验，已形成产学研用一体化的科研攻坚力量。</div>
                    <br><h3>专利与软著 </h3>
                    <div class="text">
                        <h5>实用新型专利</h5>
                        一种计算机数据备份智能防护设备<br>
                       一种数据芯片安全保护罩
                        <h5>软件著作权</h5>
                       大气污染数据聚类分析系统 <br>
                       大气污染数据图表示聚类分析系统<br>
                  </div>
                </div>
            </div>

            <!--Default Service Block-->
            <div class="default-service-block col-md-4 col-sm-6 col-xs-12">
                <div class="inner-box wow fadeInUp" data-wow-delay="0ms" data-wow-duration="1500ms">
                    <div class="icon-box"><span class="flaticon-transport-1"></span></div>
                    <h3>科研项目  </h3>
                    <div class="text">

                        1. 国家自然科学基金：基于数据点相互影响的多阶密度聚类算法及其在大气污染时空数据中的应用研究 （主持）<br>
                        
                        2. 甘肃省高等学校产业支撑计划项目：基于大数据分析的兰州市房屋信息服务系统 （主持）<br>
                        
                        3. 甘肃省科技厅重点研发计划：基于图学习的大气污染数据聚类研究及其系统开发（主持）<br>
                        
                        4. 国家自然科学基金：基于自适应密度的聚类算法及其在大气污染中的应用研究（主持）<br>
                        
                        5. 甘肃省自然科学基金：基于髙维聚类和描述逻辑的兰州市大气污染源查询研究（主持）<br>
                        
                        
                        6. 甘肃省教育厅科研经费：基于单点与多点相似的聚类算法研究（主持）
                        
                      
                    </div>
                </div>
            </div>

            <!--Default Service Block-->
            <div class="default-service-block col-md-4 col-sm-6 col-xs-12">
                <div class="inner-box wow fadeInRight" data-wow-delay="0ms" data-wow-duration="1500ms">
                    <div class="icon-box"><span class="flaticon-airplane-shape"></span></div>
                    <h3>代表论文 </h3>
                    <div class="text">
                        1. Analysis of pollutants transport in heavy air pollution processes using a new complex-network-based model, Atmospheric Environment, 2023.<br>

                        2. Adaptive Graph Representation for Clustering. IEEE Access, 2022.<br>

                        3. A Novel Missing Data Imputation Approach for Time Series Air Quality Data Based on Logistic Regression, Atmosphere, 2022<br>

                        4. Detecting communities by suspecting the maximum degree nodes, International Journal of Modern Physics B, 2019<br>

                        5. Effectively Detecting Communities by Adjusting Initial Structure via Cores, Complexity, 2019<br>

                    
                    </div>
                </div>
            </div>
            

        </div>

    </div>
</section>


        
        
            <!--Default Section-->
        
        
        <!--    </footer>-->
        
        </div>
        <!--End pagewrapper-->
        
        <!--Scroll to top-->
        <div class="scroll-to-top scroll-to-target" data-target=".main-header"><span class="icon flaticon-airplane-1"></span></div>
    </div>
</template>

<script >
    export default {
        data(){
            return{
                name:"",
                login:false
            }
        },
        methods:{
            async logout() {
            this.$confirm('确定注销并退出系统吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$store.dispatch('LogOut').then(() => {
                    localStorage.clear()
                    location.href = '/';
                })
            }).catch(() => {});
            },
            moveToKMeans(){
                this.$router.push({name:'KMeans'})
            },
            moveToDBSCAN(){
                this.$router.push({name:'DBSCAN'})
            },
            moveToDP(){
                this.$router.push({name:'DP'})
            },
            moveToCLUB(){
                this.$router.push({name:'CLUB'})
            },
            moveToEPC(){
                this.$router.push({name:'EPC'})
            },
            moveToSSC(){
                this.$router.push({name:'Ssc'})
            },
            moveToLRR(){
                this.$router.push({name:'Lrr'})
            },
            moveToRcut(){
                this.$router.push({name:'Rcut'})
            },
            moveToNcut(){
                this.$router.push({name:'Ncut'})
            },
            moveToSP(){
                this.$router.push({name:'Sp'})
            },
            cluster(){
                this.$router.push({name:'cluser'})
            }
            

        },
        mounted(){
            this.name = localStorage.getItem('name')
            if(this.name){
                this.login = true
            }
                
        }
    }
</script>

<style  scoped>
    @import url('../assets/css/bootstrap.css');
    @import url('../assets/css/style.css');
    @import url('../assets/css/responsive.css');
    @import url('../assets/css/revolution-slider.css');
    
</style>