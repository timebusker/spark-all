package com.timebusker.scala

/*
  * @DESC:Scala_08_Object：对象操作
  * @author:timebusker
  * @date:2019 /7/8
  */
class Scala_09_Class {

  // 类是对象的抽象，而对象是类的具体实例。类是抽象的，不占用内存，而对象是具体的，占用存储空间。
  // 类是用于创建对象的蓝图，它是一个定义包括在特定类型的对象中的方法和变量的软件模板。

  // 在Scala中，类并不声明为public． Scala源文件可以包含多个类，所有这些类都具有公有可见性．
  // 属性不声明默认为public．

  // Scala 修饰符：
  // Private : 带有此标记的成员仅在包含了成员定义的类或对象内部可见，同样的规则还适用内部类。(只在当前类内部有效，包含内部类)
  // Protected : 只允许保护成员在定义了该成员的的类的子类中被访问。
  // Public : 如果没有指定任何的修饰符，则默认为 public。任何地方都可以被访问。
  // 作用域保护 (Private[X]，X指代某个所属的包、类或单例对象。)
  // 这种技巧在横跨了若干包的大型项目中非常有用，它允许你定义一些在你项目的若干子包中可见但对于项目外部的客户却始终不可见的东西。

  // 定义变量时必须赋值
  private var agra = 1
  protected var agrb = 2
  var agrc = 3


  // 如果字段是私有的，则getter和setter方法也是私有的
  // 如果字段是val，则只有getter方法被生成
  // 如果你不需要任何的getter和setter方法，可以将字段声明为private[this]

  // Scala可以有任意多的构造器，
  // 不过，Scala有一个构造器比其他所有构造器都重要，就是主构造器，
  // 除了主构造器之外，类还有任意多的辅助构造器．
  // 辅助构造器的名称为this;
  // 每一个辅助构造器都必须以一个先前已定义的其他辅助构造器或主构造器的调用开始

}

// 使用类的名称作为一个类构造函数，构造函数可以使用多个参数。
//
class ConstructClass(xc: Int, yc: Int) {
  private var x: Int = xc
  private var y: Int = yc

  def move(dx: Int, dy: Int): Unit = {
    // 自动类型推导需要严格注意表达式的书写，比较一下两个输出：
    println("移动的距离是：（" + x + "\t," + y + "）\tTO\t（" + (x + dx) + "\t," + (y + dy) + "）")
    println("移动的距离是：（" + x + "\t," + y + "）\tTO\t（" + x + dx + "\t," + y + dy + "）")
  }
}

object testClassOps {
  def main(args: Array[String]): Unit = {
    var clazz = new Scala_09_Class
    // private、protected均不能被访问
    // println(clazz.agra + "\t" + clazz.agrb + "\t" + clazz.agrc)
    println("设置前：" + clazz.agrc + "\t设置后：" + (clazz.agrc = 333))
    clazz.agrc = 333
    println("设置前：" + clazz.agrc + "\t设置后：")

    var construct = new ConstructClass(1, 2);
    construct.move(2, 1)
  }
}
