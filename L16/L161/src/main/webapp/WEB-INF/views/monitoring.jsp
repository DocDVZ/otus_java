<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <title>Simple MBean monitoring tool</title>
    <!--<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">-->
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="/resources/monitoringPageScript.js"></script>
    <link type="text/css" href="/resources/background.css" rel="stylesheet"/>
    <link type="text/css" href="/resources/monitoring.css" rel="stylesheet"/>
</head>
    <body>
        <div class="top">
            <img src="/resources/logo.png"/>
            <br/>
            <a href="/">Home</a>
        </div>
        <div>
            <button id="getBeansButton" onclick="buttonClick()">Get MBeans</button>
        </div>

        <div id="accordion">
        </div>

    </body>
</html>