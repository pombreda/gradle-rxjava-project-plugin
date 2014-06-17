/*
 * Copyright 2014 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nebula.plugin.rxjavaproject

import nebula.test.IntegrationSpec

class RxjavaProjectPluginLauncherSpec extends IntegrationSpec {

    def setup() {
        writeHelloWorld('reactivex')
        createFile('src/examples/java/Example.java') << 'public class Example {}'
        createFile('src/perf/java/Perf.java') << 'public class Perf {}'

        buildFile << """
            ${applyPlugin(RxjavaProjectPlugin)}
            apply plugin: 'java'
        """.stripIndent()
    }

    def 'stand it all up'() {
        when:
        def result = runTasksSuccessfully('build')

        then:
        fileExists('build/classes/examples/Example.class')
        fileExists('build/classes/perf/Perf.class')
        fileExists('build/classes/main/reactivex/HelloWorld.class')
        fileExists('build/docs/javadoc/index.html')
        new File(projectDir, 'build/docs/javadoc/index.html').text.contains('<title>RxJava Javadoc unspecified</title>')
        fileExists('build/libs/stand-it-all-up-javadoc.jar')
        fileExists('build/libs/stand-it-all-up-sources.jar')
        fileExists('build/libs/stand-it-all-up-tests.jar')
        fileExists('build/libs/stand-it-all-up-benchmarks.jar')
        fileExists('build/libs/stand-it-all-up.jar')
    }

}