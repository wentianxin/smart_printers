package com.qg.smpt.web.repository;

import com.qg.smpt.web.model.Printer;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface PrinterMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Printer record);

    int insertSelective(Printer record);

    Printer selectByPrimaryKey(Integer id);

    Printer selectPrinter(Integer id);

    int updateByPrimaryKeySelective(Printer record);

    int updateByPrimaryKey(Printer record);
    
    int addUserPrinterBatch(List<Printer> printers);
    
    int insertPrinterBatch(List<Printer> printers);
}