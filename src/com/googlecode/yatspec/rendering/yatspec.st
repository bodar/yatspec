<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
    <title>$testSuite$</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js" type="text/javascript"></script>

    $customHeaderContent$

    $script:{
        <script type="text/javascript">
        // <![CDATA[
            $it$
        // ]]>
        </script>
    }$

    $stylesheet:{
        <style type="text/css">
            /* <![CDATA[ */
            $it$
            /* ]]> */
        </style>
    }$
</head>
<body>

<h1>$testResult.name$</h1>

$if(testResult.notes)$
    <pre class="notes">$testResult.notes$</pre>
$endif$

$if(testResult.link)$
    <a href="$testResult.link.href$">$testResult.link.text$</a>
$endif$

<div class="contents">
    <h2>Contents</h2>

    <ul class="contents">
        $testResult.testMethods:{ testMethod |
            <li class='$cssClass.(testMethod.status)$'><a href="#$testMethod.name$">$testMethod.displayName$</a></li>
        }$
    </ul>

</div>

    $testResult.testMethods:{ testMethod |
        <div class="testmethod">

        <a id="$testMethod.name$"></a>
        <h2>$testMethod.displayName$</h2>

            $if(testMethod.notes)$
                <pre class="notes">$testMethod.notes$</pre>
            $endif$
            
            $if(!testMethod.scenarioTable.empty)$
                <h3>Scenarios</h3>

                <table class="scenarios">
                    <thead>
                    <tr>
                        $testMethod.scenarioTable.displayHeaders: { header | <th>$header$</th> }$
                    </tr>
                    </thead>
                    <tbody>
                    $testMethod.scenarios, testMethod.scenarioTable.rows: { scenario, row |
                        <tr>
                            $row: { value | <td class='$cssClass.(scenario.status)$'><a href="#$scenario.name$">$value$</a></td> }$
                        </tr>
                     }$
                    </tbody>
                </table>
            $endif$

            $testMethod.scenarios: { scenario |
                <div class="scenario" id="$testMethod.uid$_$scenario.uid$">
                    <a id="$scenario.name$"></a>
                    <h2>Specification</h2>
                    <pre class="highlight specification">
                        $scenario.specification;separator="\n"$</pre>

                    <h2>Test results:</h2>


                    <pre class='highlight results $cssClass.(scenario.status)$'>
                        $scenario.message$</pre>

                     $if(!scenario.interestingGivens.keys.empty)$
                         <h3 class="logKey">Interesting Givens</h3>

                        <table class="interestingGivens logValue">
                        $scenario.interestingGivens.keys:{ key |
                            <tr>
                                <th class="key">$key$</th>
                                <td class="interestingGiven">$scenario.interestingGivens.(key)$</td>
                            </tr>
                        }$
                        </table>
                    $endif$


                    $scenario.logs.keys:{ key |
                        <h3 class="logKey" logkey="$key.valueWithSpacesReplacedByUnderscore$">$key.value$</h3>
                        <div class="logValue highlight xml $scenario.logs.(key).class.simpleName$" >$scenario.logs.(key)$</div>
                    }$
            </div>
            }$

        </div>
    }$

</body>
</html>
