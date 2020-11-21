# LL（1）分析

利用Java语言实现程序：通过LL（1）分析法对输入的字符串进行归纳分析



## 功能

1. 将非LL（1）文法转化为LL（1）文法
   * 消除直接左递归
   * 消除间接左递归
   * 消除回溯（利用first集）
   * 解决$匹配问题
2. LL（1）分析
   * 求First集
   * 求Follow集
   * 求预测分析表（构造预测分析表）
   * 利用符号栈对输入用户输入的字符串进行归纳分析



## 开发环境

* 集成开发环境 IntellijIdea 2020.2
* Java版本：JDK 14
* 项目管理：maven
* 项目测试：Junit 5



## 关键算法介绍

* 求first集

  1. 先对文法进行初始化，对于文法的每一个符号，构造相应的first集（类对象）

     * 非终结符：first集合元素为空
     * 终结符：first集合的元素唯一且为本身

  2. 扫描一遍文法的所有产生式  `->` 右边的第一个符号是终结符，就将其加到 `->` 左边非终结符的 first集中

     * eg：`A->aB...` 则  `first(A) = {..,a}`

  3. 循环扫描文法的产生式 n 次，重复执行下面的操作，直到没有新的元素被加到文法任何符号的first集合中

     * 将 `->` 右边第一个（非终结符）的first集添加到 `->`左边的非终结符的first集（除去空字符 ‘$’）里面

       eg：`A->B..`  则  `first(A) = first(A) + first(B) - '$'`

     * 如果 `->` 左边的前k个非终结符可以推出 `$` ，就将下一个紧挨着的非终结符的first集添加到 `->` 左边非终结符字符的first集里

       eg：`A->..BCD... '$'∈ first(B) '$'∈ first(C)`   则   `first(A) = first(A) + first(D) - '$'`

     * 特别的如果 `->` 右边的所有非终结符都可以推导出 `$` ，则将 `$`  加入到 `->` 左边非终结符的 first集

       eg： `A->BC '$'∈ first(B) '$'∈ first(C)`   则   `first(A) = {..,$}` 

* 求 follow 集

  follow集的求解依赖于first集，所以在求follow集之前，必须先求文法各个符号的first集

  1. 初始化文法每一个非终结符的follow集

  2. 将 `#` 符号加入到开始符号的follow集中

  3. 循环遍历下面的操作直到没有任何新的follow集元素被加到follows集合中

     * 对于每一个产生式，挨个符号遍历，并且把当前符号下一个符号的 first 集 加到 当前符号的 follow 集中

       eg： `A->..BC..`  则  `follow(B) = follow(B) + first(A)`

     * 对于每一个产生式，从右往左遍历，把 `->` 左边非终结符的 follow集 加到 当前符号的 follow集中

       eg：`A->..B`  则  `follow(B) = follow(B) + follow(A)`

     * 如果右边的 n 个非终结符的 first 集合 包含 `$`  集， 就将 `->` 左边非终结符的 follow 集加到 紧挨着的 follow集中

       eg：`A->..BC   $∈first(C)`  则 `follow(B) = follow(B) + follow(A)`

