package com.impfac

import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem
import org.apache.poi.ss.usermodel.FormulaEvaluator

class Runner {

    static void main(def args){
        def directory = new File(".")

        while(true){
            def xlsFiles = directory
                    .list(
                    [accept:{d, f-> f.endsWith(".xls") }] as FilenameFilter
            ).toList()


            xlsFiles.eachWithIndex { file, i ->
                println "${i} - ${file}"
            }

            println "${xlsFiles.size()} - Salir"
            println ""

            def valorIngresado = System.console().readLine "Ingrese el numero del archivo a elegir: "
            def optionSelected = Integer.parseInt(valorIngresado)

            println ""
            if (optionSelected == xlsFiles.size()) {
                println "Saliendo..."
                return
            } else {
                println "Opcion Seleccionada: ${optionSelected} => ${xlsFiles.get(optionSelected)}"
                processSheat(directory, xlsFiles.get(optionSelected))
            }

        }

    }

    private static void processSheat(directory, filename) {
        def file = new File(directory, filename)

        println "-------------- ${file.absolutePath} -----------------------------"
        def fs = new NPOIFSFileSystem(file)
        def wb = new HSSFWorkbook(fs.getRoot(), true)
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator()

        def mapper = new RowReader(evaluator: evaluator)
                .addReader("A", "tipoDocumento")
                .addReader("B", "nroDocumento")
                .addReader("C", "item")
                .addReader("F", "posicionArancelaria")
                .addReader("G", "unidadMedida")
                .addReader("H", "cantidad")
                .addReader("I", "monto")
                .addReader("J", "tipoCambio")
                .addReader("K", "aduanaDeRegistro")
                .addReader("L", "aduanaDeSalida")
                .addReader("M", "fecha")

        def sheet = wb.getSheetAt(0)

        def fileNameOutput = "${filename.substring(0, filename.length() - 4)}.txt"
        println "Write in ${fileNameOutput}"
        new File(directory, fileNameOutput).newOutputStream().withWriter("UTF-8") { writer ->
            sheet.each { row ->
                def record = new RecordRubro3()
                mapper.loadRowIn(row, record)
                println "To write Row: ${String.valueOf(row.getRowNum()).padLeft(2, '0')}: ${record}"
                assert record.toString().length() == 89
                writer.write(record.toString() + "\r\n")
            }
        }
        fs.close()
    }
}
