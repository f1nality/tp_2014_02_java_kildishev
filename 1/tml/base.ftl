<#macro body title="Home">
<!DOCTYPE html>
<html>
<head>
    <title>Java <#if title??> - ${title}</#if></title>
    <link href="/css/bootstrap.css" rel="stylesheet" media="screen">
    <link href="/css/style.css" rel="stylesheet" media="screen">
</head>
<body>
    <div class="container">
        <h1>ru.tech-mail.java</h1>
        <#nested/>
    </div>

    <script src="/js/jquery-2.1.0.min.js"></script>
    <script src="/js/bootstrap.min.js"></script>
</body>
</html>
</#macro>