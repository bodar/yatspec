<!DOCTYPE>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
    <head>
        <title>$testSuite$</title>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js" type="text/javascript"></script>

        $customHeaderContent:{
        $it$
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

        $testResult.annotations:{ annotation |
        <pre class="notes">$annotation$</pre>
        }$

        <div class="contents">
            <h2>Contents</h2>

            <ul class="contents">
                $testResult.testMethods:{ testMethod |
                    <li class='$cssClass.(testMethod.status)$'><a href="#$testMethod.name$">$testMethod.displayName$</a></li>
                }$
            </ul>
        </div>

        $testResult.testMethods:{ testMethod |
            <div class="testmethod section">
                <a id="$testMethod.name$"></a>
                <h2 class="section-header">$testMethod.displayName$</h2>
                <div class="section-body">
                    $testMethod.annotations:{ annotation |
                        <pre class="notes">$annotation$</pre>
                    }$

                    $if(!testMethod.scenarioTable.empty)$
                        <h3>Scenarios</h3>

                        <table class="scenarios">
                            <thead>
                            <tr>
                                $testMethod.scenarioTable.headers: { header | <th>$header$</th> }$
                            </tr>
                            </thead>
                            <tbody>
                            $testMethod.scenarios, testMethod.scenarioTable.rows: { scenario, row |
                                <tr>
                                    $row: { value |
                                        <td class='$cssClass.(scenario.status)$'><a href="#$scenario.name$">
                                            $if(value.empty)$
                                                (empty)
                                            $else$
                                                $value$
                                            $endif$
                                        </a></td> }$
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
                                $scenario.specification$</pre>
                            <h3 class="logKey">Test results:</h3>
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
                                        <div class="logValue highlight $scenario.logs.(key).class.simpleName$" >$scenario.logs.(key)$</div>
                                    }$
                    </div>
                    }$
                </div>
            </div>
        }$

    <div class="footer">
        $script:{
        <script type="text/javascript">
            // <![CDATA[
            $it$
            // ]]>
        </script>
        }$
    </div>

    </body>
</html>
