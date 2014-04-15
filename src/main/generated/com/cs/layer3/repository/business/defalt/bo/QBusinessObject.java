package com.cs.layer3.repository.business.defalt.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QBusinessObject is a Querydsl query type for BusinessObject
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QBusinessObject extends BeanPath<BusinessObject> {

    private static final long serialVersionUID = -1059329573L;

    public static final QBusinessObject businessObject = new QBusinessObject("businessObject");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final SimplePath<Iterable> referencedList = createSimple("referencedList", Iterable.class);

    public QBusinessObject(String variable) {
        super(BusinessObject.class, forVariable(variable));
    }

    public QBusinessObject(Path<? extends BusinessObject> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBusinessObject(PathMetadata<?> metadata) {
        super(BusinessObject.class, metadata);
    }

}

