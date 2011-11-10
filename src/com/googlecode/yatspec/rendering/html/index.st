<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>Yatspec Index</title>
    $stylesheet:{
        <style type="text/css">
            /* <![CDATA[ */
            $it$
            /* ]]> */
        </style>
    }$
</head>
<body>

<h1>Index</h1>

<div class="contents">
    <ul class="contents">
        $results:{ result |
            <li class="$cssClass.(result.status)$"><a href="$result.url$" >$result.name$</a></li>
        }$
    </ul>
</div>
</body>
</html>
