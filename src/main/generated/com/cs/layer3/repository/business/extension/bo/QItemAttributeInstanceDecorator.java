package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QItemAttributeInstanceDecorator is a Querydsl query type for ItemAttributeInstanceDecorator
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QItemAttributeInstanceDecorator extends EntityPathBase<ItemAttributeInstanceDecorator> {

    private static final long serialVersionUID = -1717931414L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemAttributeInstanceDecorator itemAttributeInstanceDecorator = new QItemAttributeInstanceDecorator("itemAttributeInstanceDecorator");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final QAttributeInstance attributeInstance;

    public final com.cs.layer3.repository.business.defalt.bo.QEntity entity;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public QItemAttributeInstanceDecorator(String variable) {
        this(ItemAttributeInstanceDecorator.class, forVariable(variable), INITS);
    }

    public QItemAttributeInstanceDecorator(Path<? extends ItemAttributeInstanceDecorator> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QItemAttributeInstanceDecorator(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QItemAttributeInstanceDecorator(PathMetadata<?> metadata, PathInits inits) {
        this(ItemAttributeInstanceDecorator.class, metadata, inits);
    }

    public QItemAttributeInstanceDecorator(Class<? extends ItemAttributeInstanceDecorator> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attributeInstance = inits.isInitialized("attributeInstance") ? new QAttributeInstance(forProperty("attributeInstance"), inits.get("attributeInstance")) : null;
        this.entity = inits.isInitialized("entity") ? new com.cs.layer3.repository.business.defalt.bo.QEntity(forProperty("entity")) : null;
    }

}

