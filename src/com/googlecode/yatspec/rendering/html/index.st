<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Yatspec Index</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js" type="text/javascript"></script>
    $stylesheet:{
        <style type="text/css">
            /* <![CDATA[ */
            $it$
            /* ]]> */
        </style>
    }$
    $script:{
        <script type="text/javascript">
        // <![CDATA[
            $it$
        // ]]>
        </script>
    }$
</head>
<body>

<h1>Index</h1>

<div class="contents">
    <h2>Tests</h2>
    <ul>
        $result.packages:indexPackage(package=it, cssClass=cssClass)$
    </ul>
    <h2>Tags</h2>
    <ul>
        $result.tags:indexTag(tag=it, cssClass=cssClass)$
    </ul>
</div>
</body>
</html>
