package com.ronnev.math

/**
 * A tiny class that extends a list with four combinatorial operations:
 * ''combinations'', ''subsets'', ''permutations'', ''variations''.
 *
 * You can find all the ideas behind this code at blog-post:
 *
 *   http://vkostyukov.ru/posts/combinatorial-algorithms-in-scala/
 *
 * How to use this class.
 *
 *   import CombinatorialOps._
 *   val v = List(1, 2, 3).xvariations(2)
 *
 * The following functions are available:
 *
 *  - xcombinations(n) -- generates n-combinations
 *  - xsubsets         -- generates all subsets
 *  - xvariations(n)   -- generates n-variations
 *  - xpermutations    -- generates all permutations
 *
 */

object CombinatorialOps {

  implicit class CombinatorialList[A](l: List[A]) {

    /**
     * A pre-calculated size of given list.
     */
    val xsize = l.size

    /**
     * Generates the combinations of this list with given length 'n'. The order
     * doesn't matter.
     *
     * The total number of k-combinations on n-length set might be calculated
     * as follows:
     *
     *                  C_k,n = n!/k!(n - k)!
     *
     * Time - O(C_k,n)
     * Space - O(C_k,n)
     */
    def xcombinations(n: Int): List[List[A]] =
      if (n > xsize) Nil
      else l match {
        case _ :: _ if n == 1 => l.map(List(_))
        case hd :: tl => tl.xcombinations(n - 1).map(hd :: _) ::: tl.xcombinations(n)
        case _ => Nil
      }

    /**
     * Generates all the subsets of this list. The order doesn't matter.
     *
     * The total number of subsets might be obtained from variations formula:
     *
     *                  S_n = sum(i=1..n) {C_i,n} = 2 ** n

     * Time - O(S_n)
     * Space - O(S_n)
     */
    def xsubsets: List[List[A]] =
      (2 to xsize).foldLeft(l.xcombinations(1))((a, i) => l.xcombinations(i) ::: a)

    /**
     * Generates the variations of this list with given length 'n'. The order
     * does matter.
     *
     * The total number of variations might be calculated as follows:
     *
     *                   V_k,n = n!/(n - k)!
     *
     * Time - O(V_k,n)
     * Space - O(V_k,n)
     */
    def xvariations(n: Int): List[List[A]] = {
      def mixmany(x: A, ll: List[List[A]]): List[List[A]] = ll match {
        case hd :: tl => foldone(x, hd) ::: mixmany(x, tl)
        case _ => Nil
      }

      def foldone(x: A, ll: List[A]): List[List[A]] =
        (1 to ll.length).foldLeft(List(x :: ll))((a, i) => (mixone(i, x, ll)) :: a)

      def mixone(i: Int, x: A, ll: List[A]): List[A] =
        ll.slice(0, i) ::: (x :: ll.slice(i, ll.length))

      if (n > xsize) Nil
      else l match {
        case _ :: _ if n == 1 => l.map(List(_))
        case hd :: tl => mixmany(hd, tl.xvariations(n - 1)) ::: tl.xvariations(n)
        case _ => Nil
      }
    }

    /**
     * Generates all permutations of this list. The order does matter.
     *
     * The total number of permutations might be calculated as follows:
     *
     *                 P_n = V_n,n = n!
     *
     * Time - O(n!)
     * Space - O(n!)
     */
    def xpermutations: List[List[A]] = xvariations(xsize)
  }
}