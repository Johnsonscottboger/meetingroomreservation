package com.aning.meetingroomreservation.service

import com.aning.meetingroomreservation.entity.User
import java.io.File

/**
 * 提供 [User] 实体的导入服务
 */
public interface IUserImportService {

    /**
     * 解析指定的文件
     * @param fileName 指定的文件
     * @return 文件中包含的 [User] 记录
     */
    public fun parse(fileName: String): List<User> = parse(File(fileName))

    /**
     * 解析指定的输入流
     * @param file 指定的输入流
     * @return 输入流中包含的 [User] 记录
     */
    public fun parse(file: File): List<User>
}