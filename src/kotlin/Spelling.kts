import java.io.File
import extensions.or
import java.util.HashMap
import java.util.Arrays

// credits;
// http://norvig.com/spell-correct.html
// kotlin implementation by mark kranz

object Model {
    var NWORDS: Map<String, Int> = HashMap<String,Int>()

    fun load(filename: String) {
        var file = File("big.txt")
        NWORDS = file
                .readLines()
                .flatMap{line -> line.split(" ")
                        .filter{word -> word.matches("[a-z]+")}
                        .map{word -> word.toLowerCase()} }
                .groupBy { word -> word }
                .mapValues { kv -> kv.value.size  }
    }

    fun edits1(word: String) : List<String> {
        var nonEmpty = {(p : Pair<String,String>) -> p.second.length > 0}
        var alphabet = 'a'..'z'
        var splits = (0..word.size).map{i -> Pair(word.substring(0,i),word.substring(i))}
        var deletes = splits.filter(nonEmpty).map{p -> p.first + p.second.substring(1)}
        var transposes =  splits.filter{p -> p.second.length > 1}.map{p -> p.first +p.second[1] + p.second[0]+p.second.substring(2)}
        var replaces = alphabet.flatMap{letter -> splits.filter(nonEmpty).map{p -> p.first + letter + p.second.substring(1)}}
        var inserts = alphabet.flatMap{letter -> splits.map{p -> p.first + letter + p.second }}

        return deletes + transposes + replaces + inserts;
    }

    fun edits2(word: String): Iterable<String> {
        return edits1(word).flatMap{w -> edits1(w)}
    }

    fun known(words: Iterable<String>): Iterable<String> {
        return words.filter{word -> NWORDS.containsKey(word)}
    }

    fun known(vararg words : String): Iterable<String> {
        return known(words.toList())
    }

    fun max(a : String, b : String) : String {
        return if(NWORDS[a] ?: 0 > NWORDS[b] ?: 0) a else b
    }

    fun correct(word: String): String {
        var candidates = known(word) or known(edits1(word)) or known(edits2(word)) or word
        return candidates.reduce { (a,b) -> max(a,b) }
    }
}

fun main(args: Array<String>): Unit {
    Model.load("big.txt")

    println("poteto -> " + Model.correct("poteto"))
    println("korrecter -> " + Model.correct("korrecter"))
}