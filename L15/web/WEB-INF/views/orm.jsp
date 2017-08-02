<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8"/>
    <title>ORM</title>
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="/resources/ormPageScript.js"></script>
    <link type="text/css" href="/resources/background.css" rel="stylesheet"/>
    <link type="text/css" href="/resources/orm.css" rel="stylesheet"/>
</head>
<body>
<div class="top">
    <img src="/resources/logo.png"/>
    <br/>
    <a href="/index.html">Home</a>
</div>
<div class="operation">
    <h2>Create/update operation</h2>
    <div>
        <form id="newEntityForm">
            intField:<br>
            <input type="number" name="intField"/>
            strField:<br>
            <input type="text" name="strField"/>
            boolField:<br>
            <input type="checkbox" name="boolField"/>
            longField:<br>
            <input type="number" name="longField"/>
            bigDecimalField:<br>
            <input type="number" step="0.01" name="bigDecimalField"/>
            bigIntegerField:<br>
            <input type="number" name="bigIntegerField"/>
            dateField:<br>
            <input type="datetime-local" name="dateField"/>
            <button type="button" id="createButton" onclick="newSimpleEntity()">Create/update entity</button>
        </form>
    </div>


</div>
<div class="operation">
    <h2>Read operation</h2>
    <form id="getEntityForm">
        intField:<br>
        <input type="number" name="intField" id="readInput"/>
        <button type="button" id="readButton" onclick="readSimpleEntity()">Find entity by id</button>
    </form>
</div>
<div id="results">

</div>
</body>
</html>
