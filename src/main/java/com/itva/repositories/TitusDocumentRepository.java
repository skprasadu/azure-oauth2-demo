package com.itva.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itva.model.TitusDocument;

public interface TitusDocumentRepository extends JpaRepository<TitusDocument, Long> {

}
