package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QItemClazzAttributeDecorator is a Querydsl query type for ItemClazzAttributeDecorator
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QItemClazzAttributeDecorator extends EntityPathBase<ItemClazzAttributeDecorator> {

    private static final long serialVersionUID = 1899181815L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemClazzAttributeDecorator itemClazzAttributeDecorator = new QItemClazzAttributeDecorator("itemClazzAttributeDecorator");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final com.cs.layer3.repository.business.defalt.bo.QAttribute attribute;

    public final com.cs.layer3.repository.business.defalt.bo.QClazz clazz;

    public final com.cs.layer3.repository.business.defalt.bo.QEntity entity;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public final StringPath value = createString("value");

    public QItemClazzAttributeDecorator(String variable) {
        this(ItemClazzAttributeDecorator.class, forVariable(variable), INITS);
    }

    public QItemClazzAttributeDecorator(Path<? extends ItemClazzAttributeDecorator> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QItemClazzAttributeDecorator(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QItemClazzAttributeDecorator(PathMetadata<?> metadata, PathInits inits) {
        this(ItemClazzAttributeDecorator.class, metadata, inits);
    }

    public QItemClazzAttributeDecorator(Class<? extends ItemClazzAttributeDecorator> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.attribute = inits.isInitialized("attribute") ? new com.cs.layer3.repository.business.defalt.bo.QAttribute(forProperty("attribute")) : null;
        this.clazz = inits.isInitialized("clazz") ? new com.cs.layer3.repository.business.defalt.bo.QClazz(forProperty("clazz")) : null;
        this.entity = inits.isInitialized("entity") ? new com.cs.layer3.repository.business.defalt.bo.QEntity(forProperty("entity")) : null;
    }

}

