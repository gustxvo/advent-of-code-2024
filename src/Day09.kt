fun main() {

    fun blockFiles(input: String): List<Int> = input.flatMapIndexed { index, blockSize ->
        List(blockSize.digitToInt()) {
            if (index % 2 == 0) index / 2 else -1
        }
    }

    fun List<Int>.orderedBlockFiles(): List<Int> {
        val diskMap = this.toMutableList()
        while (diskMap.contains(-1)) {
            val nextFreeSpaceIndex = diskMap.indexOfFirst { it == -1 }
            val lastFileBlockIndex = diskMap.indexOfLast { it != -1 }
            val lastFileBlock = diskMap[lastFileBlockIndex]
            diskMap[nextFreeSpaceIndex] = lastFileBlock
            diskMap.removeAt(lastFileBlockIndex)
        }
        return diskMap
    }

    fun firstFreeBlockOrNull(blockFiles: List<Int>, blockSize: Int): IntRange? {
        return blockFiles.windowed(blockSize)
            .indexOfFirst { block -> block.all { it == -1 } }
            .takeIf { it != -1 }
            ?.let { i -> i..<i + blockSize }
    }

    fun part1(input: String): Long {
        return blockFiles(input).orderedBlockFiles()
            .foldIndexed(0) { index, acc, id ->
                acc + index * id
            }
    }

    fun part2(input: String): Long {
        val blockFiles = blockFiles(input).toMutableList()

        var index = blockFiles.indexOfLast { it != -1 }
        while (index > blockFiles.indexOf(-1)) {
            val id = blockFiles[index]
            if (id != -1) {
                val fileSize = index - blockFiles.indexOf(id) + 1
                val firstFreeBlock = firstFreeBlockOrNull(blockFiles, fileSize)
                if (firstFreeBlock != null && index > firstFreeBlock.first) {
                    for (i in firstFreeBlock) {
                        blockFiles[i] = id
                    }
                    val fileBlockRange = index - fileSize + 1..index
                    for (i in fileBlockRange) {
                        blockFiles[i] = -1
                    }
                }
                index -= fileSize
            } else {
                index--
            }
        }
        return blockFiles.foldIndexed(0) { index, acc, id ->
            if (id == -1) acc else acc + index * id
        }
    }

    val input = readTextInput("Day09")
    part1(input).println()
    part2(input).println()
}

