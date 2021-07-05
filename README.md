# Curriculum-design-of-Compilers-Principles
  简要介绍：这是由广西大学计算机科学与技术系的三位同学编写的编译原理课程设计。使用java编写的c语言编译原理玩具，将c语言程序翻译到简单汇编指令，满足编译原理课程设计编写需求。
# 词法分析
  首先在input.txt写下你的c语言程序，可以参考example.txt，毕竟是玩具，考虑的语法没有那么多。
  然后运行lexer生成LexerOutput.txt用于识别各个词。
# 语法分析
  使用lr1语法分析，具体这个玩具有什么语法可以去grammar.xls看，根据grammar.xls手动编写的lr1.xls表，要修改语法的话慎重。
  运行lr1，同时完成语法分析和语义分析阶段，生成中间代码即四元式在SemanticOutput.txt。
# 语义分析与中间代码生成
  具体实现在Semantics,并搭载在lr1中，运行lr1后已经生成四元式。
# 四元式翻译成汇编
  读取SemanticOutput.txt，生成简单汇编指令（基于王爽那本书的8086cpu的汇编指令）。
  运行Complie完成翻译过程
# 最后提示一下
  由于需要读取excel表，用到poi-3.9.jar（在lib下），如果你第一次运行需要配置就自己配置一下，我用的idea。
