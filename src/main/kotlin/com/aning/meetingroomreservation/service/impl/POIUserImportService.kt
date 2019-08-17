package com.aning.meetingroomreservation.service.impl

import com.aning.meetingroomreservation.entity.User
import com.aning.meetingroomreservation.service.IUserImportService
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream

/**
 * 使用 POI 实现用户导入服务
 */
@Service
public class POIUserImportService : IUserImportService {

    /**
     * 解析指定的输入流
     * @param file 指定的输入流
     * @return 输入流中包含的 [User] 记录
     */
    override fun parse(file: File): List<User> {
        val extension = file.extension
        val fileStream = FileInputStream(file)
        var workbook: Workbook? = null
        var evaluator: FormulaEvaluator? = null
        when {
            extension.equals("xls", true) -> {
                workbook = HSSFWorkbook(fileStream)
                evaluator = HSSFFormulaEvaluator(workbook)
            }
            extension.equals("xlsx", true) -> {
                workbook = XSSFWorkbook(fileStream)
                evaluator = XSSFFormulaEvaluator(workbook)
            }
            else -> throw IllegalArgumentException("不支持的文件")
        }
        val sheet = workbook.getSheetAt(0)
        val list = mutableListOf<User>()
        var rowIndex = -1
        var columnIndex = -1
        var nameColumnIndex = -1
        var departmentColumnIndex = -1
        try {
            for (row in sheet) {
                rowIndex++
                columnIndex = -1
                var name = ""
                var department = ""
                for (cell in row.cellIterator()) {
                    columnIndex++
                    val value = getCellValue(evaluator, cell)
                    if (rowIndex == 0) {
                        when (value) {
                            "姓名" -> nameColumnIndex = columnIndex
                            "部门" -> departmentColumnIndex = columnIndex
                        }
                    } else {
                        if (nameColumnIndex == -1)
                            throw IllegalArgumentException("未找到\"姓名\"列")
                        if (departmentColumnIndex == -1)
                            throw IllegalArgumentException("未找到\"部门\"列")
                        if (name.isNotBlank() && department.isNotBlank())
                            break
                        when (columnIndex) {
                            nameColumnIndex -> {
                                if (value.isBlank())
                                    return list
                                name = value
                            }
                            departmentColumnIndex -> {
                                if (value.isBlank())
                                    return list
                                department = value
                            }
                        }
                    }
                }
                if (name.isNotBlank() && department.isNotBlank()) {
                    list.add(User(name = name, department = department))
                }
            }
        } finally {
            fileStream.close()
            workbook.close()
        }
        return list
    }

    /**
     * 获取单元格的显示值
     * @param evaluator 单元格公式计算器
     * @param cell 单元格
     * @return 单元格显示的值
     */
    private fun getCellValue(evaluator: FormulaEvaluator, cell: Cell): String {
        return when (cell.cellType) {
            CellType.BLANK -> ""
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.ERROR -> cell.errorCellValue.toString()
            CellType.FORMULA -> getCellValue(evaluator, evaluator.evaluateInCell(cell))
            CellType.NUMERIC -> if (DateUtil.isCellDateFormatted(cell))
                cell.dateCellValue.toString()
            else
                cell.numericCellValue.toString()
            CellType.STRING -> cell.stringCellValue
            else -> ""
        }
    }
}